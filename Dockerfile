FROM openjdk:17.0.1-jdk-slim AS builder

WORKDIR /app

COPY src /app/src
COPY pom.xml .
COPY .mvn /app/.mvn
COPY mvnw .
RUN apt-get update && apt-get install -y dos2unix
RUN dos2unix mvnw && chmod +x mvnw 
RUN ls -l
RUN ./mvnw package -DskipTests

FROM eclipse-temurin:17-jdk-jammy

COPY --from=builder /app/target/*.jar /app/spring-petclinic.jar

CMD [ "java", "-jar", "/app/spring-petclinic.jar" ]
