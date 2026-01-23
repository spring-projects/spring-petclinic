# use the light wight jdk 17 base image
FROM eclipse-temurin:17-jdk-alpine
# Set working directory inside the container

WORKDIR /app
COPY target/spring-petclinic-4.0.0-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
