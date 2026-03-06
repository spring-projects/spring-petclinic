# ----------------------------
# Stage 1 — Build
# ----------------------------
FROM maven:3.9.9-eclipse-temurin-17-alpine AS builder

LABEL maintainer="Qoseem"

WORKDIR /build

# Copy pom.xml first (better caching)
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Build application
RUN mvn clean package -DskipTests


# ----------------------------
# Stage 2 — Runtime
# ----------------------------
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Create non-root user
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Copy jar from builder stage
COPY --from=builder /build/target/*.jar app.jar

# Change ownership
RUN chown -R appuser:appgroup /app

# Run as non-root user
USER appuser

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]