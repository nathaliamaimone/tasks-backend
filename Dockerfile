FROM tomcat:9.0.98-jdk21-temurin

ARG WAR_FILE
ARG CONTEXT

COPY  ${WAR_FILE} /usr/local/tomcat/webapps/${CONTEXT}.war
