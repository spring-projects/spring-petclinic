FROM ubuntu:latest

# Install OpenJDK 17 and Maven
RUN apt-get update && \
    apt-get -y install openjdk-17-jdk-headless maven

# Set working directory
WORKDIR /app

# Copy code to container
COPY . .

# Build application
RUN ./mvnw package -Dmaven.test.skip=true

# Run application
CMD ["java", "-jar", "target/*.jar"]
