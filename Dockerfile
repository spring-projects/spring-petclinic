# Stage 1: Build Stage
FROM maven:3.9.4-eclipse-temurin-17 AS build

# Set the working directory inside the build stage
WORKDIR /app

# Copy the Maven project files to the build stage
COPY pom.xml .
COPY src ./src

# Build the project and create the jar
RUN mvn clean package -DskipTests

# Stage 2: Runtime Stage
FROM openjdk:17-alpine

# Set the working directory inside the runtime stage
WORKDIR /app

# Copy the built jar from the build stage
COPY --from=build /app/target/spring-petclinic-4.0.2-SNAPSHOT.jar /app/

# Command to run the application
CMD ["java", "-jar", "spring-petclinic-4.0.2-SNAPSHOT.jar"]

