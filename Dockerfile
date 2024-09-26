#stage 1
# Use a base image with OpenJDK
FROM openjdk:17-jdk-slim as build

# Set the working directory
WORKDIR /app

# Install Maven
RUN apt-get update && \
    apt-get install -y maven && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Copy the project files into the container
COPY . .

# Package the application using Maven
RUN ./mvnw package

#stage 2
# Stage 2: Create a Distroless image
FROM gcr.io/distroless/java17

# Set the working directory
WORKDIR /app

# Copy the packaged JAR from the build stage
COPY --from=build /app/target/spring-petclinic-3.3.0-SNAPSHOT.jar app.jar

#Expose port
EXPOSE 8080

#cmd command
CMD ["app.jar"]
