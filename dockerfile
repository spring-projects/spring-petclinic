# Build stage
FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml . 
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM openjdk:17-jdk-slim AS runtime
WORKDIR /app
COPY --from=build /app/target/*.jar /app/spring-petclinic.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/spring-petclinic.jar"]
