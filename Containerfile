# Start with a base image containing Java runtime 
FROM openjdk:21
 
# Add Maintainer Info 
LABEL maintainer="waseemahammed96@gmail.com" 
 
# Add a volume pointing to /mnt/artefact
VOLUME /mnt/artefact 
 
# Make port 8080 available to the world outside this container 
EXPOSE 8080
 
# The application's jar file 
ARG JAR_FILE=$PWD/target/*.jar
 
# Add the application's jar to the container 
ADD ${JAR_FILE} spring-petclinic-service.jar
 
# Run the jar file 
ENTRYPOINT ["java","-Dserver.port=8080","-Dspring.profiles.active=default","-jar","/spring-petclinic-service.jar"]
