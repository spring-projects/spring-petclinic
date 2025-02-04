FROM gradle:8.12-jdk17-focal AS build

RUN mkdir /project
COPY . /project
WORKDIR /project
RUN gradle clean build

FROM eclipse-temurin:17.0.14_7-jre-ubi9-minimal

RUN mkdir /app
COPY --from=build /project/build/libs/spring-petclinic-0.1.3-SNAPSHOT.jar /app/java-application.jar
WORKDIR /app
CMD [ "java", "-jar", "java-application.jar"]