FROM tomcat:9.0-alpine

COPY target/petclinic.war /usr/local/tomcat/webapps/petclinic.war
