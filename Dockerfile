FROM eclipse-temurin:24.0.1_9-jre-alpine-3.21@sha256:dbc9b392f33b2aca2c3d47de4534f3453e75d3b6dd27e08a555a47369be9b49c

RUN addgroup -S appgroup && adduser -S appuser -G appgroup

WORKDIR /app

COPY --chown=appuser:appgroup ./target/*.jar app.jar

RUN chmod 500 app.jar

USER appuser

EXPOSE 8080

ENTRYPOINT [ "java", "-jar", "app.jar" ]

