# Use OpenJDK 17
FROM openjdk:17

# Set working directory
WORKDIR /app

# Copy Maven-built jar into container
COPY target/*.jar app.jar

# Expose the port your app will run on
EXPOSE 9090

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
