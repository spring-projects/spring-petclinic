FROM maven:3.8.3-openjdk-17-slim AS builder
WORKDIR /app
COPY . .                       
RUN mvn package -DskipTests  


FROM gcr.io/distroless/java17
COPY --from=builder /app/target/spring-petclinic*.jar /app/app.jar
CMD ["app.jar"]
