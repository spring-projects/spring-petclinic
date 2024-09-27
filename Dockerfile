# Use an official OpenJDK 17 image as a base
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven wrapper files
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Grant execution permission to Maven wrapper
RUN chmod +x ./mvnw

# Download and install Maven dependencies (will be cached if unchanged)
RUN ./mvnw dependency:go-offline

# Copy the remaining project files
COPY src ./src

# Build the application using Maven wrapper
RUN ./mvnw clean package -DskipTests

# Expose the port on which the application will run
EXPOSE 8080

# Run the Spring Boot application with the correct JAR version
ENTRYPOINT ["java", "-jar", "target/spring-petclinic-3.3.0-SNAPSHOT.jar", "--spring.profiles.active=mysql"]
