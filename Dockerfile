# Alpine Linux with OpenJDK JRE
FROM openjdk:8-jre-alpine

EXPOSE 8080

# copy jar into image
COPY target/*.war /usr/bin/spring-petclinic.war

# run application with this command line 
ENTRYPOINT ["java","-jar","/usr/bin/spring-petclinic.war","--server.port=8080"]
