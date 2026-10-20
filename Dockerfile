FROM eclipse-temurin:17-jdk-jammy AS build

WORKDIR /build
COPY . .
RUN sed -i 's/\r$//' mvnw \
    && chmod +x mvnw \
    && ./mvnw -B clean package -DskipTests
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /build/target/spring-petclinic-*.jar app.jar
USER 10001:10001
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
