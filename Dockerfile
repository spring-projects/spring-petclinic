FROM amazoncorretto:21-alpine3.21-jdk
LABEL authors="rdiach"

COPY ./build/libs/spring-petclinic-3.4.0.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar", "-Dspring.profiles.active='postgres"]
