# Use an official OpenJDK runtime as a parent image
FROM openjdk:11

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR file from the build context
COPY target/*.jar app.jar

# Run the jar file
CMD ["java", "-jar", "app.jar"]

# Expose the port the app runs on
EXPOSE 8080
