pipeline {
    agent any
    stages {
        stage('Build Backend') {
            steps {
                bat 'mvnw clean package -DskipTests=true'
            }
        }
        stage('Unit Tests') {
            steps {
                bat 'mvn test'
            }
        }
        stage('SonarQube Analysis') {
            environment {
                scannerHome = tool 'SONAR_SCANNER'
            }
            steps {
                withSonarQubeEnv('SONAR_LOCAL') {
                    bat """
                        ${scannerHome}/bin/sonar-scanner -e \
                        -Dsonar.projectKey=DeployBack \
                        -Dsonar.sources=. \
                        -Dsonar.host.url=http://localhost:9000 \
                        -Dsonar.login=sqp_a1bef6d9ca69cc9e213d3b06b608933292386935 \
                        -Dsonar.java.binaries=target
                    """
                }
            }
        }
        stage('Quality Gate') {
            steps {
                sleep(12)
                timeout(time: 1, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: false, credentialsId: 'TokenSonar'
                }
            }
        }
        stage('Deploy Backend') {
            steps {
               deploy adapters: [tomcat9(credentialsId: 'TomcatLogin', path: '', url: 'http://localhost:8001/')], contextPath: 'tasks-backend', war: 'target/tasks-backend.war'
            }
        }
        stage('API Tests') {
            steps {
                dir('api-test') {
                    git branch: 'master', url: 'https://github.com/nathaliamaimone/tasks-api-test'
                    bat 'mvn test'           
                }
            }
        }
        stage('Deploy Frontend') {
            steps {
                dir('tasks-frontend') {
                    git branch: 'master', url: 'https://github.com/nathaliamaimone/tasks-frontend'
                    bat 'mvn clean package'
                    deploy adapters: [tomcat9(credentialsId: 'TomcatLogin', path: '', url: 'http://localhost:8001/')], contextPath: 'tasks', war: 'target/tasks.war'           
                }
            }
        }
        stage('Functional Tests') {
            steps {
                dir('functional-test') {
                    git branch: 'main', url: 'https://github.com/nathaliamaimone/tasks-functional-tests'
                    bat 'mvn test'           
                }
            }
        }
        stage('Deploy Production') {
            steps {
                bat 'docker-compose build'
                bat 'docker-compose up -d'
            }
        }
        stage('Health Check') {
            steps {
                sleep(10)
                dir('functional-test') {
                    bat 'mvn verify -Dskip.surefire.tests'           
                }
            }
        }
    }
    post {
        always {
            junit allowEmptyResults: true, 
                  testResults: '''
                      target/surefire-reports/*.xml,
                      api-test/target/surefire-reports/*.xml,
                      functional-test/target/surefire-reports/*.xml,
                      functional-test/target/failsafe-reports/*.xml,
                      tasks-frontend/target/surefire-reports/*.xml,
                      tasks-backend/target/surefire-reports/*.xml
                  '''
        }
        unsuccessful {
            emailext attachLog: true, body: 'See the attached log for details', subject: 'Build $BUILD_NUMBER has failed', to: 'nathaliamaimone.qa@gmail.com'
        }
        fixed {
            emailext attachLog: true, body: 'See the attached log for details', subject: 'Build $BUILD_NUMBER is fine!', to: 'nathaliamaimone.qa@gmail.com'
        }
    }
}
