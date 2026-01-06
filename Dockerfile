FROM openjdk:26-ea-17-jdk-slim-trixie

WORKDIR /app

COPY . .

EXPOSE 8080

CMD [ "./mvnw", "spring-boot:run" ]