# STAGE 1: Build
FROM gradle:8.14.2-jdk17 AS builder

WORKDIR /build
COPY . .
RUN gradle clean build -x test

RUN cp /build/target/spring-petclinic-*.jar /build/app.jar

# STAGE 2: Runtime
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app
COPY --from=builder /build/app.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]