# -------------------------
# Stage 1: Build
# -------------------------
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

# Copy Maven configuration and wrapper first (cache dependencies)
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Download dependencies (cached if pom.xml doesn't change)
RUN mvn dependency:go-offline -B || true

# Copy source code
COPY src ./src

# Optional: fix formatting
RUN mvn spring-javaformat:apply -B || true

# Build the project (skip tests)
RUN mvn clean package -DskipTests -B

# -------------------------
# Stage 2: Runtime
# -------------------------
FROM eclipse-temurin:17

WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Run the application
CMD ["java", "-jar", "app.jar"]
