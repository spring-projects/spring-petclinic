FROM gradle:8-jdk-alpine AS build
WORKDIR /app

COPY . /app

RUN gradle clean build

FROM openjdk:17-jdk-slim
WORKDIR /app

COPY --from=build /app/build/libs/*.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
