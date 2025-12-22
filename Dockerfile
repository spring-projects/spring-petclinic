# syntax=docker/dockerfile:1.6

ARG JAVA_VERSION=21
ARG MAVEN_IMAGE=maven:3.9-eclipse-temurin-21
ARG JDK_IMAGE=eclipse-temurin:21-jdk
ARG DISTROLESS_IMAGE=gcr.io/distroless/base-debian12:nonroot

# =========================
# 1) Build
# =========================
FROM ${MAVEN_IMAGE} AS builder
WORKDIR /app

COPY pom.xml ./
RUN --mount=type=cache,target=/root/.m2 \
    mvn -q -DskipTests dependency:go-offline

COPY . .
RUN --mount=type=cache,target=/root/.m2 \
    mvn -q -DskipTests package

RUN set -eux; \
    JAR="$(ls -1 target/*.jar | head -n 1)"; \
    test -n "$JAR"; \
    cp -f "$JAR" /app/app.jar

# =========================
# 2) Whatap Agent stage
# =========================
FROM ${JDK_IMAGE} AS whatap_agent
WORKDIR /work

COPY whatap/whatap.agent.java.tar.gz /tmp/whatap.agent.java.tar.gz
COPY paramkey.txt /tmp/paramkey.txt

RUN set -eux; \
    mkdir -p /whatap; \
    tar -xzf /tmp/whatap.agent.java.tar.gz -C /whatap; \
    rm -f /tmp/whatap.agent.java.tar.gz; \
    # agent jar 찾기
    AGENT_JAR="$(find /whatap -maxdepth 6 -type f \
      \( -name 'whatap.agent*.jar' -o -name '*whatap*agent*.jar' \) | head -n 1)"; \
    test -n "$AGENT_JAR"; \
    cp -f "$AGENT_JAR" /whatap/whatap.agent.jar; \
    cp -f /tmp/paramkey.txt /whatap/paramkey.txt; \
    chmod -R a=rX /whatap

# =========================
# 3) jlink Slim JRE
# =========================
FROM ${JDK_IMAGE} AS jre
WORKDIR /jrebuild

COPY --from=builder /app/app.jar ./app.jar

RUN set -eux; \
    DEPS="$(jdeps --ignore-missing-deps --multi-release=21 --print-module-deps app.jar)"; \
    jlink --strip-debug --no-man-pages --no-header-files --compress=2 \
      --add-modules "$DEPS,\
java.desktop,java.management,jdk.management,java.sql,java.naming,\
java.logging,java.security.jgss,jdk.security.auth,java.security.sasl,java.instrument,\
jdk.crypto.ec,jdk.unsupported,java.xml" \
      --output /opt/jre

# =========================
# 4) Runtime (Distroless)
# =========================
FROM ${DISTROLESS_IMAGE}
WORKDIR /app

COPY --from=jre     /opt/jre    /opt/jre
COPY --from=builder /app/app.jar ./app.jar

# ✅ 핵심: /whatap 을 nonroot(65532)가 소유하도록 복사
COPY --from=whatap_agent --chown=65532:65532 /whatap /whatap

ENV JAVA_HOME=/opt/jre
ENV PATH="/opt/jre/bin:${PATH}"

# ✅ whatap home 명시(에이전트가 내부 파일 쓸 때 경로 확정)
ENV WHATAP_HOME=/whatap

ENV JAVA_TOOL_OPTIONS="\
-javaagent:/whatap/whatap.agent.jar \
-Dwhatap.paramkey=/whatap/paramkey.txt \
--add-opens=java.base/java.lang=ALL-UNNAMED \
-XX:+UseContainerSupport \
-XX:MaxRAMPercentage=75 \
-XX:+ExitOnOutOfMemoryError \
-XX:+AlwaysActAsServerClassMachine"

EXPOSE 8080
ENTRYPOINT ["/opt/jre/bin/java", "-jar", "/app/app.jar"]
