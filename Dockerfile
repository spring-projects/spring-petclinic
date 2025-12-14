FROM eclipse-temurin:25-jdk-alpine AS builder
WORKDIR /workspace

# Copy Maven wrapper and configuration files first for better layer caching
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Download dependencies (this layer will be cached if pom.xml doesn't change)
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

# Copy source code
COPY src src

# Build the application
RUN ./mvnw -B -DskipTests package

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Install wget for healthcheck
RUN apk add --no-cache wget

# Create a non-root user for security
RUN addgroup -S spring && adduser -S spring -G spring

# Set environment variables
ENV SPRING_PROFILES_ACTIVE=${SPRING_PROFILES_ACTIVE:-mysql}
ENV JAVA_OPTS=""

# Copy the JAR file from builder
COPY --from=builder /workspace/target/*.jar app.jar

# Change ownership to non-root user
RUN chown spring:spring app.jar

# Switch to non-root user
USER spring:spring

# Expose the application port
EXPOSE 8080

# Add healthcheck
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Use exec form for better signal handling
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

