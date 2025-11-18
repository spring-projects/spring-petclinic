FROM eclipse-temurin:25-jdk-alpine AS builder
WORKDIR /workspace

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN chmod +x mvnw \
    && ./mvnw -B -DskipTests package

FROM eclipse-temurin:25-jre-alpine
WORKDIR /app

ENV SPRING_PROFILES_ACTIVE=${SPRING_PROFILES_ACTIVE:-mysql}
ENV JAVA_OPTS=""

COPY --from=builder /workspace/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

