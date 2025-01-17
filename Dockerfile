FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/spring-petclinic-*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
