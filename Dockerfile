FROM openjdk:8u171-jre-alpine

RUN apk add --no-cache bash

WORKDIR /opt

COPY target/spring-petclinic-2.1.0.BUILD-SNAPSHOT.jar .

ENV JAVA_OPTS="-Xms32m -Xmx128m"

ENTRYPOINT exec java $JAVA_OPTS -jar spring-petclinic-2.1.0.BUILD-SNAPSHOT.jar
