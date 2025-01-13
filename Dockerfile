# Use an OpenJDK base image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Define a build argument for the artifact version
ARG ARTIFACT_VERSION

# Copy the JAR file into the image dynamically using the provided version
COPY target/spring-petclinic-${ARTIFACT_VERSION}.jar app.jar

# Expose port 80 for the application
EXPOSE 80

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
