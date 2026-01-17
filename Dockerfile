# STAGE 1: Build
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /build
COPY . .
RUN mvn clean package -DskipTests
RUN cp /build/target/spring-petclinic-5.1.0-SNAPSHOT.jar /build/app.jar

# STAGE 2: Runtime
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=builder /build/app.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]