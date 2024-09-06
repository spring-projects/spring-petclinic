FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy the application JAR file 
COPY spring-petclinic-3.3.0-SNAPSHOT.jar /app/spring-petclinic.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "spring-petclinic.jar"]
