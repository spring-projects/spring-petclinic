FROM openjdk:8-jdk-alpine

COPY target/*.jar /opt/app.jar
WORKDIR /opt
CMD ["java", "-jar", "app.jar"]
