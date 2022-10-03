FROM openjdk:11-slim
COPY target/*.jar .
ENTRYPOINT [ "java" , "-jar" , "./spring-petclinic-2.7.3.jar"]