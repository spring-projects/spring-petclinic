FROM gradle:8-jdk17-jammy@sha256:cb3b50c6d5298026e5962880469079d62389f33744af3bba90bf21175052aa91 AS build

RUN mkdir /project
COPY . /project
WORKDIR /project

RUN gradle clean build -x check -x test



FROM gcr.io/distroless/java17

EXPOSE 8080
WORKDIR /app

COPY --from=build /project/build/libs/spring-petclinic-*.jar /app/java-application.jar
COPY --from=build /project/metrics /app/metrics

ENTRYPOINT [ "java", "-Dspring.profiles.active=mysql", "-jar", "java-application.jar"]
