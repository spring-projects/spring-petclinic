FROM maven AS builder

WORKDIR /app

COPY . .

RUN ["mvn", "clean", "install", "-DskipTests"]

FROM gcr.io/distroless/java21-debian12

WORKDIR /app

COPY --from=builder /app/target/spring-petclinic-*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]