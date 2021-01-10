FROM maven:3.6.3-jdk-8 AS build-env
WORKDIR /app

COPY pom.xml ./
RUN mvn dependency:go-offline

COPY . ./
RUN mvn validate compile test
RUN mvn package

FROM openjdk:8-jre-alpine

WORKDIR /app
COPY --from=build-env /app/target/spring-petclinic-2.4.0.BUILD-SNAPSHOT.jar ./spring-petclinic.jar

RUN apt-get update && apt-get install -y curl
RUN curl
CMD ["java", "-jar", "/app/spring-petclinic.jar"]
EXPOSE 8080
