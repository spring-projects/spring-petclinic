# Use an official OpenJDK image as a base
FROM openjdk:17-slim

# Set the working directory
WORKDIR /app

# Copy the pom.xml and other necessary files first
COPY . .

# Install Maven
RUN apt-get update && \
    apt-get install -y maven && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Expose the port
EXPOSE 8080

# Run Maven to build the application
RUN ./mvnw package




# Set the command to run the application
CMD ["java", "-jar", "target/spring-petclinic-3.3.0-SNAPSHOT.jar"]
