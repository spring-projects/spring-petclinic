FROM openjdk:8-jdk-alpine

LABEL Gabri Botha <bothagabri@gmail.com>

ENV TZ Africa/Johannesburg
ENV DEPLOY_STAGE staging

WORKDIR /app
COPY target/* /app/

ENTRYPOINT ["java","-jar","/app/*.jar"]