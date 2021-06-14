# syntax=docker/dockerfile:1
FROM openjdk:16-alpine3.13
WORKDIR /app
COPY target/spring-petclinic-2.4.5.jar . 
CMD [ "java", "-jar spring-petclinic-2.4.5.jar" ]
