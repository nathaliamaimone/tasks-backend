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
                    bat "${scannerHome}/bin/sonar-scanner -e -Dsonar.projectKey=DeployBack -Dsonar.sources=. -Dsonar.host.url=http://localhost:9000 -Dsonar.login=sqp_a1bef6d9ca69cc9e213d3b06b608933292386935 -Dsonar.java.binaries=target"
                }
                
            }
        }
        stage('Quality Gate') {
            steps {
                timeout(time: 1, unit: 'HOURS') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }
    }    
}