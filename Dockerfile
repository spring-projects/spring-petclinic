
# Stage 1: Build application
FROM eclipse-temurin:17-jdk-jammy AS build

WORKDIR /app

COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

RUN chmod +x mvnw

COPY src/ src/

RUN ./mvnw -B -DskipTests package


# Stage 2: Run application
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

COPY --from=build --chown=10001:10001 /app/target/spring-petclinic-*.jar app.jar

USER 10001:10001

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
