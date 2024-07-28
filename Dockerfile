# Use an official OpenJDK runtime as a parent image
FROM openjdk:11-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file to the app directory (adjust this path according to your actual JAR file name in the target directory)
COPY target/*.jar app.jar

# Run the jar file
CMD ["java", "-jar", "app.jar"]

# Expose the port the app runs on
EXPOSE 8080
