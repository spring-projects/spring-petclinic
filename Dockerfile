FROM maven:3.5-jdk-8-alpine AS build
COPY src /src
COPY pom.xml /
RUN mvn -f pom.xml clean package

FROM openjdk:8-jdk-alpine
COPY --from=build /target/spring-petclinic-2.6.0-SNAPSHOT.jar spring-petclinic-2.6.0-SNAPSHOT.jar
ENTRYPOINT ["java","-jar", "spring-petclinic-2.6.0-SNAPSHOT.jar"]
