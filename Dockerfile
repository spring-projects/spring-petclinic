# =================
# Stage 1: Build
# =================
# Use a specific, trusted builder image that includes Maven and the required JDK.
# This keeps our build environment consistent and separate from our runtime.
FROM maven:3.9.6-eclipse-temurin-17-focal AS builder

# Set the working directory for the build stage.
WORKDIR /source

# Copy the project's source code into the build stage.
COPY . .

# Run the Maven package command to compile the code and build the executable JAR.
# This also downloads dependencies, which are cached in this layer.
# -DskipTests speeds up the build process in CI/CD.
RUN mvn package -DskipTests

# =================
# Stage 2: Runtime
# =================
# Use a minimal, secure Java Runtime Environment (JRE) for the final image.
# This dramatically reduces the image size and attack surface.
FROM eclipse-temurin:17-jre-focal

# Set the working directory for the runtime container.
WORKDIR /app

# Copy ONLY the built JAR file from the 'builder' stage into our final image.
# This is the key to a small and secure final image.
COPY --from=builder /source/target/spring-petclinic-*.jar app.jar

# Expose the port the application runs on (default for Spring Boot is 8080).
EXPOSE 8080

# Define the command to run the application when the container starts.
ENTRYPOINT ["java", "-jar", "app.jar"]