FROM openjdk:8-jdk-alpine
COPY spring-petclinic-2.5.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-Dspring.profiles.active=mysql","-jar","/app.jar"]