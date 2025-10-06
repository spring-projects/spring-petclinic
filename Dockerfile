FROM eclipse-temurin:17-jdk AS builder

WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
RUN ./mvnw dependency:go-offline -B

COPY src src

RUN ./mvnw package -DskipTests



FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=builder /workspace/app/target/spring-petclinic-*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
