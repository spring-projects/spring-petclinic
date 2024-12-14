FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

COPY target/*.jar spring-petclinic.jar

ENTRYPOINT ["java", "-jar", "spring-petclinic.jar"]

EXPOSE 8080
