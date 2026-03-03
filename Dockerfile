# Use lightweight Java 17 runtime
FROM eclipse-temurin:17-jdk

# Create working directory inside container
WORKDIR /app

# Copy built jar from host to container
COPY target/*.jar app.jar

# Expose application port
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java","-jar","app.jar"]
