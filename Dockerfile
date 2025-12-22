# syntax=docker/dockerfile:1.6

ARG JAVA_VERSION=21
ARG MAVEN_IMAGE=maven:3.9-eclipse-temurin-${JAVA_VERSION}
ARG JDK_IMAGE=eclipse-temurin:${JAVA_VERSION}-jdk
ARG RUNTIME_IMAGE=eclipse-temurin:${JAVA_VERSION}-jre

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
    mvn -q -DskipTests clean package

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
    AGENT_JAR="$(find /whatap -maxdepth 6 -type f \
      \( -name 'whatap.agent*.jar' -o -name '*whatap*agent*.jar' \) | head -n 1)"; \
    test -n "$AGENT_JAR"; \
    cp -f "$AGENT_JAR" /whatap/whatap.agent.jar; \
    cp -f /tmp/paramkey.txt /whatap/paramkey.txt; \
    chmod -R a=rX /whatap

# =========================
# 3) Runtime (fallback-first: temurin JRE)
# =========================
# 기본 목표는 “정상 기동”이므로 디버깅이 용이한 eclipse-temurin JRE를 사용한다.
# distroless/jlink 최적화는 별도 커밋에서 재적용한다.
FROM ${RUNTIME_IMAGE}
WORKDIR /app

COPY --from=builder /app/app.jar /app/app.jar
COPY --from=whatap_agent /whatap /whatap

ENV JAVA_TOOL_OPTIONS="" \
    JAVA_OPTS="" \
ENABLE_WHATAP=false \
WHATAP_HOME=/whatap

# ENTRYPOINT wrapper: ENABLE_WHATAP=true 일 때만 agent 옵션을 추가한다.
COPY <<'EOF' /entrypoint.sh
#!/bin/sh
set -eu

JAVA_ARGS=${JAVA_OPTS:-}
case "${ENABLE_WHATAP:-false}" in
  true|TRUE|True)
  JAVA_ARGS="$JAVA_ARGS -javaagent:/whatap/whatap.agent.jar"
  JAVA_ARGS="$JAVA_ARGS -Dwhatap.paramkey=/whatap/paramkey.txt"
  JAVA_ARGS="$JAVA_ARGS -Dwhatap.server.host=${WHATAP_SERVER_HOST:-}"
  JAVA_ARGS="$JAVA_ARGS -Dlicense=${WHATAP_LICENSE:-}"
  JAVA_ARGS="$JAVA_ARGS -Dwhatap.micro.enabled=${WHATAP_MICRO_ENABLED:-}"
  JAVA_ARGS="$JAVA_ARGS --add-opens=java.base/java.lang=ALL-UNNAMED"
  ;;
esac

exec java $JAVA_ARGS -XX:+UseContainerSupport -XX:MaxRAMPercentage=75 \
  -XX:+ExitOnOutOfMemoryError -XX:+AlwaysActAsServerClassMachine \
  -jar /app/app.jar
EOF

RUN chmod +x /entrypoint.sh

EXPOSE 8080
ENTRYPOINT ["/entrypoint.sh"]
