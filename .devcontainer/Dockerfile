# Build stage
FROM openjdk:17-jdk-slim as builder

WORKDIR /app

# Copy Maven wrapper and POM
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# Download dependencies
RUN ./mvnw dependency:go-offline

# Copy source code
COPY src ./src

# Build the application
RUN ./mvnw package -DskipTests

# Run stage
FROM openjdk:17-jre-slim

WORKDIR /app

# Copy the built artifact from builder stage
COPY --from=builder /app/target/*.jar app.jar

# Container runs on port 8080
EXPOSE 8080

# Set the startup command
ENTRYPOINT ["java", "-jar", "app.jar"]
