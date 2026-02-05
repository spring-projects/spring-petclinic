# ---------- 1) BUILD STAGE: Build the Spring PetClinic app with Maven ----------
FROM eclipse-temurin:17-jdk as build

# Install Maven Wrapper dependencies if needed
WORKDIR /app

# Copy only build-related files first to leverage Docker layer caching
COPY mvnw ./
COPY .mvn .mvn
COPY pom.xml ./

# Pre-fetch dependencies (faster builds)
RUN chmod +x mvnw && ./mvnw -B -q -DskipTests dependency:go-offline

# Now copy the sources and build the JAR
COPY src ./src
RUN ./mvnw -B -DskipTests package

# Find the built jar (petclinic usually builds to target/*.jar)
# We'll move it to a predictable location.
RUN mkdir -p /out && cp target/*.jar /out/app.jar


# ---------- 2) RUNTIME STAGE: Run the app with OpenTelemetry agent ----------
FROM eclipse-temurin:17-jre as runtime

RUN apt-get update && apt-get install -y jq && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# ---- OpenTelemetry Java Agent version (editable) ----
ARG OTEL_JAVA_AGENT_VERSION=1.33.4
ENV OTEL_AGENT_DIR=/otel
RUN mkdir -p ${OTEL_AGENT_DIR}

# Download the OpenTelemetry Java agent
# You can change the version above if needed.
ADD https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v${OTEL_JAVA_AGENT_VERSION}/opentelemetry-javaagent.jar ${OTEL_AGENT_DIR}/otel-agent.jar

# Copy app jar from the build stage
COPY --from=build /out/app.jar /app/app.jar

# ---- Default OpenTelemetry env vars (override in Render) ----
# Service name as it will appear in Dynatrace
ENV OTEL_SERVICE_NAME=petclinic
# Set resource attributes (useful for filtering/grouping in Dynatrace)
ENV OTEL_RESOURCE_ATTRIBUTES=service.namespace=demo,service.version=1.0.0,deployment.environment=render
# Export traces (and optionally metrics/logs) to Dynatrace OTLP endpoint
# You WILL override the endpoint and headers in Render dashboard.
ENV OTEL_TRACES_EXPORTER=otlp
ENV OTEL_METRICS_EXPORTER=none
ENV OTEL_LOGS_EXPORTER=none

# JVM options: attach the OTel Java agent
ENV JAVA_TOOL_OPTIONS="-javaagent:/otel/otel-agent.jar"

# Copy log forwarder
COPY forward-logs.sh /app/forward-logs.sh
RUN chmod +x /app/forward-logs.sh

# Run PetClinic and log forwarder in same container
ENTRYPOINT ["bash", "-c", "java -jar app.jar & /app/forward-logs.sh"]

# Expose PetClinic default port (usually 8080)
EXPOSE 8080

# Health check (optional, adjust if your app exposes actuator/health)
# HEALTHCHECK --interval=30s --timeout=5s --start-period=60s --retries=5 \
#   CMD curl -fsS http://localhost:8080/ || exit 1

# Start the app

# ---------- ADDITIONS FOR OPTION 2 LOG FORWARDING ----------

# Copy log-forwarding script into container
COPY forward-logs.sh /app/forward-logs.sh

# Make the script executable
RUN chmod +x /app/forward-logs.sh

# Run both PetClinic and log forwarder in same container
ENTRYPOINT ["bash", "-c", "java -jar app.jar & /app/forward-logs.sh"]

