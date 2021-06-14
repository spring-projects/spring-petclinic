# syntax=docker/dockerfile:1
FROM java:8
WORKDIR /
COPY target/spring-petclinic-2.4.5.jar . 
EXPOSE 8080
CMD [ "java", "-jar", "spring-petclinic-2.4.5.jar" ]
