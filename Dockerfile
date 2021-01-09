FROM maven:3.6.3-jdk-8 AS build-env

COPY . ./

RUN mvn package

FROM openjdk:8-jre-alpine
EXPOSE 8080

COPY --from=build-env /target/spring-petclinic-2.4.0.BUILD-SNAPSHOT.jar ./spring-petclinic.jar
CMD ["java", "-jar", "/spring-petclinic.jar"]
