FROM eclipse-temurin:17-jre
EXPOSE 8082

ENV OTEL_SERVICE_NAME=PetClinic
ENV OTEL_EXPORTER_OTLP_ENDPOINT=http://host.docker.internal:5050
ENV OTEL_LOGS_EXPORTER="otlp"
ENV OTEL_METRICS_EXPORTER=none

ENV CODE_PACKAGE_PREFIXES="org.springframework.samples.petclinic"
ENV DEPLOYMENT_ENV="SAMPLE_ENV"

ADD target/spring-petclinic-3.1.0-SNAPSHOT-new.jar /app.jar

HEALTHCHECK --interval=20s --timeout=3s --start-period=10s --retries=4 \
  CMD curl -f http://localhost:8082/ || exit 1

ENTRYPOINT java -jar app.jar
