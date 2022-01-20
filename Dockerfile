FROM tomcat:8.0.20-jre8
COPY target/spring-petclinic-2.6.0-SNAPSHOT.jar  /usr/local/tomcat/webapps/spring-petclinic-2.6.0-SNAPSHOT.jar


