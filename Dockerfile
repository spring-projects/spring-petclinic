FROM maven:3.9.9-eclipse-temurin-17 AS builder

WORKDIR /build

COPY . .

RUN ./mvnw package -DskipTests

FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

COPY --from=builder build/target/spring-petclinic-4.0.1.jar /app/spring-petclinic.jar

ENTRYPOINT ["java", "-jar", "/app/spring-petclinic.jar"]

EXPOSE 8080