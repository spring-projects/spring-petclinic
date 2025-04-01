# Use a Maven image with JDK 17 for the build stage
FROM maven:3.9.4-eclipse-temurin-17-alpine AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY . .
RUN mvn clean package -DskipTests

# Use JDK 17 for the runtime stage
FROM eclipse-temurin:17-jdk-alpine AS runtime
WORKDIR /app
COPY --from=build /app/target/spring-petclinic-*.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]