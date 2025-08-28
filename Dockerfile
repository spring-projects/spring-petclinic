FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /build
COPY . .
RUN ./mvnw package

FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app
COPY --from=build /build/target/spring-petclinic-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]