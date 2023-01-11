FROM openjdk:17-jdk as builder

RUN mkdir -p /app/source

COPY . /app/source

WORKDIR /app/source

RUN ./mvnw clean package


FROM openjdk:17-jdk-alpine

COPY --from=builder /app/source/target/*.jar /app/app.jar

CMD ["java", "-jar",  "/app/app.jar"]
