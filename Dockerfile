# Step 1: Run SonarQube to check the code
FROM sonarqube:latest AS sonarqube
# Customize SonarQube settings if needed

# Step 2: Build the artifact
FROM maven:3.8.4-openjdk-8 AS builder
COPY . /app
WORKDIR /app
RUN mvn clean package

# Step 3: Create the final image
FROM openjdk:8-jre-alpine
COPY --from=builder /app/target/*.jar /code/
WORKDIR /code
CMD ["java", "-jar", "*.jar"]
