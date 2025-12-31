FROM registry.access.redhat.com/ubi9/openjdk-17-runtime:latest AS runtime

ARG APP_JAR=target/app.jar
ARG COMMIT_SHA=unknown
ARG VERSION=unknown

ENV APP_HOME=/opt/app \
    JAVA_OPTS="-XX:InitialRAMPercentage=50 -XX:MaxRAMPercentage=75 -XX:+ExitOnOutOfMemoryError"

WORKDIR ${APP_HOME}

COPY --chown=185:0 ${APP_JAR} app.jar

LABEL org.opencontainers.image.version="${VERSION}" \
      org.opencontainers.image.revision="${COMMIT_SHA}" 

EXPOSE 8080
USER 185

ENTRYPOINT ["sh", "-c", "exec java ${JAVA_OPTS} -jar ${APP_HOME}/app.jar"]
