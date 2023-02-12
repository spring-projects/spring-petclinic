FROM openjdk:8-jdk-alpine
COPY target/spring-petclinic-3.0.0-SNAPSHOT.jar spring-petclinic-3.0.0-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/spring-petclinic-3.0.0-SNAPSHOT.jar"]
