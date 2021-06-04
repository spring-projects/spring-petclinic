FROM openjdk:8-jdk-alpine

LABEL Gabri Botha <bothagabri@gmail.com>

ENV TZ Africa/Johannesburg
ENV DEPLOY_STAGE staging

COPY . /app
WORKDIR /app

ENTRYPOINT ["java","-jar","./target/*.jar"]
