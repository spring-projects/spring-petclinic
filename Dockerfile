FROM openjdk:26-ea-17-jdk-slim-trixie

WORKDIR /app

COPY . .

CMD [ "./mvnw", "spring-boot:run" ]