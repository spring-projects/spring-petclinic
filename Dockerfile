# syntax=docker/dockerfile:1.6

ARG JAVA_VERSION=21
ARG MAVEN_IMAGE=maven:3.9-eclipse-temurin-21
ARG JDK_IMAGE=eclipse-temurin:21-jdk
ARG DISTROLESS_IMAGE=gcr.io/distroless/base-debian12:nonroot

# =========================
# 1) Build (Maven + JDK 21)
# =========================
FROM ${MAVEN_IMAGE} AS builder
WORKDIR /app

# (선택) 빌드 재현성/속도 개선: mvnw 쓰는 프로젝트면 mvnw로 바꾸는 것도 좋음
COPY pom.xml ./
RUN --mount=type=cache,target=/root/.m2 \
    mvn -q -DskipTests dependency:go-offline

COPY . .
RUN --mount=type=cache,target=/root/.m2 \
    mvn -q -DskipTests package

# 빌드 산출물 jar 경로를 고정해두면 이후 stage에서 글로빙이 안전
RUN set -eux; \
    JAR="$(ls -1 target/*.jar | head -n 1)"; \
    test -n "$JAR"; \
    cp -f "$JAR" /app/app.jar


# =========================
# 2) Whatap Agent stage (JDK 21)
#   - tar.gz 풀어서 whatap.agent.jar 고정 파일명으로 복사
#   - paramkey.txt 포함
# =========================
FROM ${JDK_IMAGE} AS whatap_agent
WORKDIR /work

COPY whatap/whatap.agent.java.tar.gz /tmp/whatap.agent.java.tar.gz
# paramkey.txt 는 repo root에 둔다고 했으니 그대로
COPY paramkey.txt /tmp/paramkey.txt

RUN set -eux; \
    mkdir -p /whatap; \
    tar -xzf /tmp/whatap.agent.java.tar.gz -C /whatap; \
    rm -f /tmp/whatap.agent.java.tar.gz; \
    # tar 구조가 /whatap/whatap/... 인 케이스가 많아서 find로 jar 탐색
    AGENT_JAR="$(find /whatap -maxdepth 4 -type f \
      \( -name 'whatap.agent*.jar' -o -name '*whatap*agent*.jar' \) \
      | head -n 1)"; \
    test -n "$AGENT_JAR"; \
    cp -f "$AGENT_JAR" /whatap/whatap.agent.jar; \
    cp -f /tmp/paramkey.txt /whatap/paramkey.txt; \
    # distroless에서 읽기만 하면 되니 권한을 보수적으로
    chmod 0444 /whatap/whatap.agent.jar /whatap/paramkey.txt


# =========================
# 3) jlink Slim JRE (JDK 21)
#   - app.jar 기준으로 필요한 module 계산
# =========================
FROM ${JDK_IMAGE} AS jre
WORKDIR /jrebuild

COPY --from=builder /app/app.jar ./app.jar

RUN set -eux; \
    DEPS="$(jdeps --ignore-missing-deps --multi-release=21 --print-module-deps app.jar)"; \
    # Spring/Tomcat/Whatap가 건드릴 수 있는 모듈을 보강
    jlink --strip-debug --no-man-pages --no-header-files --compress=2 \
      --add-modules "$DEPS,\
java.desktop,java.management,jdk.management,java.sql,java.naming,\
java.logging,java.security.jgss,jdk.security.auth,java.security.sasl,java.instrument,\
jdk.crypto.ec,jdk.unsupported" \
      --output /opt/jre


# =========================
# 4) Runtime (Distroless)
# =========================
FROM ${DISTROLESS_IMAGE}
WORKDIR /app

COPY --from=jre     /opt/jre    /opt/jre
COPY --from=builder /app/app.jar ./app.jar
COPY --from=whatap_agent /whatap /whatap

# PATH 의존 줄이기 (ENTRYPOINT에서 /opt/jre/bin/java로 고정 권장)
ENV JAVA_HOME=/opt/jre
ENV PATH="/opt/jre/bin:${PATH}"

# Dockerfile에는 "javaagent만" 기본 탑재.
# license/host/micro 같은 환경 값은 K8s env로 주입하는게 안전/유연함.
ENV JAVA_TOOL_OPTIONS="\
-javaagent:/whatap/whatap.agent.jar \
-Dwhatap.paramkey=/whatap/paramkey.txt \
-XX:+UseContainerSupport \
-XX:MaxRAMPercentage=75 \
-XX:+ExitOnOutOfMemoryError \
-XX:+AlwaysActAsServerClassMachine"

EXPOSE 8080
ENTRYPOINT ["/opt/jre/bin/java", "-jar", "/app/app.jar"]
