FROM eclipse-temurin:17-jre
EXPOSE 8082

ENV OTEL_SERVICE_NAME=PetClinic
ENV OTEL_EXPORTER_OTLP_ENDPOINT=http://host.docker.internal:5050
ENV OTEL_LOGS_EXPORTER="otlp"
ENV OTEL_METRICS_EXPORTER=none

ENV CODE_PACKAGE_PREFIXES="org.springframework.samples.petclinic"
ENV DEPLOYMENT_ENV="SAMPLE_ENV"

ADD target/spring-petclinic-*.jar /app.jar
ADD build/otel/opentelemetry-javaagent.jar /opentelemetry-javaagent.jar
ADD build/otel/digma-otel-agent-extension.jar /digma-otel-agent-extension.jar

HEALTHCHECK --interval=20s --timeout=3s --start-period=10s --retries=4 \
  CMD curl -f http://localhost:8082/ || exit 1

ENTRYPOINT java -jar -javaagent:/opentelemetry-javaagent.jar -Dotel.javaagent.extensions=/digma-otel-agent-extension.jar app.jar
