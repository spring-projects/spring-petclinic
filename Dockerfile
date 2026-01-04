FROM registry.access.redhat.com/ubi9/openjdk-17-runtime:latest

ARG APP_JAR=target/app.jar
ARG COMMIT_SHA=unknown
ARG VERSION=0.0.0

ENV APP_HOME=/opt/app \
    JAVA_OPTS="-XX:+ExitOnOutOfMemoryError -XX:MaxRAMPercentage=75.0 -XX:InitialRAMPercentage=50.0"

LABEL org.opencontainers.image.version="${VERSION}" \
      org.opencontainers.image.revision="${COMMIT_SHA}" 

WORKDIR ${APP_HOME}

COPY ${APP_JAR} app.jar

EXPOSE 8080

CMD ["sh", "-c", "exec java ${JAVA_OPTS} -jar app.jar"]
