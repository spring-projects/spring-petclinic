
# Stage 1: Build application
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /workspace

COPY pom.xml .
COPY src ./src

RUN mvn -B -DskipTests package

# Stage 2: Runtime
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /workspace/target/spring-petclinic-4.0.0-SNAPSHOT.jar app.jar

EXPOSE 8080

# Run as non-root user
USER 10001:10001

ENTRYPOINT ["java", "-jar", "app.jar"]
