# syntax=docker/dockerfile:1

FROM gradle:jdk11-alpine@sha256:0c20c5771cbd452394e2d532592ff274daaaf026c4251a4b6988d31d82970e90 AS build

ENV HOME=/usr/project
USER root
RUN mkdir -p $HOME
WORKDIR $HOME
COPY build.gradle settings.gradle gradlew $HOME
COPY gradle $HOME/gradle

RUN ./gradlew --no-daemon build || return 0
COPY . .
RUN ./gradlew --no-daemon clean build -x test

FROM eclipse-temurin:11-jre-alpine@sha256:64674afdc275bed87f352eb94f9469842313dd2902afe9d7b0fb9508fc75cc37
RUN mkdir /app
RUN addgroup --system juser && adduser -S -s /bin/false -G juser juser
RUN apk add dumb-init
COPY --from=build /usr/project/build/libs/spring-petclinic-2.7.3.jar /app/spring-petclinic.jar
WORKDIR /app
RUN chown -R juser:juser /app
USER juser
CMD ["dumb-init", "java", "-jar", "spring-petclinic.jar"]
