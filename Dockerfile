# Use an OpenJDK base image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Define a build argument for the artifact version
ARG ARTIFACT_VERSION

ARG ARTIFACT_PATH

# Copy the JAR file into the image dynamically using the provided version
COPY ${ARTIFACT_PATH}/petclinic-${ARTIFACT_VERSION}.jar app.jar

# Expose port 80 for the application
EXPOSE 80

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
