# Build stage
FROM maven:3.9.9-eclipse-temurin-17 AS builder
WORKDIR /workspace

# Maven dependencies cachen
COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline

# Quellcode kopieren und bauen
COPY src ./src
RUN mvn -q -DskipTests package \
 && JAR="$(ls target/*.jar | grep -v 'original' | head -n 1)" \
 && cp "$JAR" target/app.jar

# Runtime stage
FROM eclipse-temurin:17-jre
WORKDIR /app

# Optional: non-root User
RUN useradd -r -u 10001 spring && mkdir -p /app && chown -R spring:spring /app
USER spring

COPY --from=builder /workspace/target/app.jar /app/app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
