# syntax=docker/dockerfile:1

FROM --platform=$BUILDPLATFORM eclipse-temurin:17.0.19_10-jdk-noble@sha256:ea77fc0dc52aeb4283cab72ef833ff77b2571eea219584d3dc75a6d4964b0ca0 AS build

WORKDIR /build

COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN --mount=type=cache,target=/root/.m2,sharing=locked \
    ./mvnw -q -B dependency:go-offline

COPY src/ src/
RUN --mount=type=cache,target=/root/.m2,sharing=locked \
    ./mvnw -q -B -DskipTests package \
    && java -Djarmode=tools -jar target/spring-petclinic-*.jar \
         extract --layers --launcher --destination /build/extracted


FROM eclipse-temurin:17.0.19_10-jre-noble@sha256:966903ee85ce45d84a3d70b2c1285a82bb70acd7dbae95b0bff509e1e92401f9 AS runtime

RUN groupadd --system --gid 10001 app \
    && useradd --system --uid 10001 --gid app --no-create-home --shell /usr/sbin/nologin app

WORKDIR /app

COPY --from=build --chown=app:app /build/extracted/dependencies/ ./
COPY --from=build --chown=app:app /build/extracted/spring-boot-loader/ ./
COPY --from=build --chown=app:app /build/extracted/snapshot-dependencies/ ./
COPY --from=build --chown=app:app /build/extracted/application/ ./

USER 10001:10001

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=5s --start-period=45s --retries=3 \
    CMD ["/bin/sh", "-c", "curl -fsS http://localhost:8080/actuator/health || exit 1"]

ARG BUILD_REVISION=unknown
ARG BUILD_VERSION=unknown
LABEL org.opencontainers.image.title="spring-petclinic" \
      org.opencontainers.image.description="Spring PetClinic - DevOps capstone build" \
      org.opencontainers.image.licenses="Apache-2.0" \
      org.opencontainers.image.base.name="eclipse-temurin:17.0.19_10-jre-noble" \
      org.opencontainers.image.revision="${BUILD_REVISION}" \
      org.opencontainers.image.version="${BUILD_VERSION}"

ENTRYPOINT ["java", \
    "-XX:MaxRAMPercentage=75.0", \
    "-XX:InitialRAMPercentage=50.0", \
    "-XX:+UseSerialGC", \
    "-XX:+ExitOnOutOfMemoryError", \
    "org.springframework.boot.loader.launch.JarLauncher"]
