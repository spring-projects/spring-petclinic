# First stage: Build the application using Maven
FROM maven:3.8.1-openjdk-17 AS build
WORKDIR /app

# Copy the Maven wrapper and the pom.xml file
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Copy the project source code
COPY src ./src

# Package the application
RUN ./mvnw package

# Second stage: Use an official OpenJDK runtime as a parent image
FROM openjdk
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Run the jar file
CMD ["java", "-jar", "app.jar"]

# Expose the port the app runs on
EXPOSE 8080
