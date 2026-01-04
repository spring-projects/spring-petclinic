FROM registry.access.redhat.com/ubi9/openjdk-17-runtime:latest

ARG APP_JAR=target/app.jar

ENV APP_HOME=/opt/app \
    JAVA_OPTS="-XX:+ExitOnOutOfMemoryError -XX:MaxRAMPercentage=75.0 -XX:InitialRAMPercentage=50.0"

WORKDIR ${APP_HOME}

# Labels are injected at image build time by the CI workflow; none defined here.
COPY ${APP_JAR} app.jar

EXPOSE 8080

CMD ["sh", "-c", "exec java ${JAVA_OPTS} -jar app.jar"]
