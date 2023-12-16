# Use an official Maven runtime as a parent image
FROM maven:3.8.4-openjdk-17-slim AS build

# Set the working directory in the container
WORKDIR /app

# Copy the compiled JAR file from the build stage
COPY target/*.jar /app/petclinic.jar

# Specify the command to run on container start
CMD ["java", "-jar", "petclinic.jar"]