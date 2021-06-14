# syntax=docker/dockerfile:1
FROM openjdk:16-alpine3.13
WORKDIR /app
COPY /var/lib/jenkins/workspace/petpetpet_main/target/spring-petclinic-2.4.5.jar . 
CMD [ "java" ]
ENTRYPOINT [ "-jar spring-petclinic-2.4.5.jar" ]
