FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /source

COPY gradlew .
COPY build.gradle .
COPY settings.gradle .
COPY gradle ./gradle 
RUN chmod 500 gradlew 

RUN ./gradlew dependencies --info --no-daemon

COPY . . 
RUN ./gradlew build --no-daemon -x test

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /source/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
