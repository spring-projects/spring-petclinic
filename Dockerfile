FROM maven:3.9.6-eclipse-temurin-21-alpine AS build

COPY . /app/spring-petclinic/
WORKDIR /app/spring-petclinic/

RUN mvn clean package

FROM openjdk:23-jdk-slim

COPY --from=build /app/spring-petclinic/target/*.jar /app/spring-petclinic.jar

WORKDIR /app

ENTRYPOINT ["java", "-jar", "spring-petclinic.jar"]
