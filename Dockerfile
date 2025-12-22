# =========================
# 1) Build (Maven + JDK 25)
# =========================
FROM maven:3.9-eclipse-temurin-25 AS builder
WORKDIR /app

COPY pom.xml ./
RUN --mount=type=cache,target=/root/.m2 \
    mvn -q -DskipTests dependency:go-offline

COPY . .
RUN --mount=type=cache,target=/root/.m2 \
    mvn -q -DskipTests package

# =========================
# 1.5) Whatap Agent unpack stage
# =========================
FROM eclipse-temurin:25-jdk AS whatap_agent
WORKDIR /whatap

# repo root 하위 whatap/whatap.agent.java.tar.gz 를 넣어둔 상태라고 했으니 그대로 복사
COPY whatap/whatap.agent.java.tar.gz /tmp/whatap.agent.java.tar.gz

RUN set -eux; \
    mkdir -p /whatap; \
    tar -xzf /tmp/whatap.agent.java.tar.gz -C /whatap; \
    rm -f /tmp/whatap.agent.java.tar.gz; \
    echo "== whatap extracted tree =="; \
    find /whatap -maxdepth 3 -type f -print; \
    AGENT_JAR="$(find /whatap -maxdepth 3 -type f \( -name 'whatap.agent*.jar' -o -name '*whatap*agent*.jar' \) | head -n 1)"; \
    test -n "$AGENT_JAR"; \
    echo "Found agent jar: $AGENT_JAR"; \
    cp -f "$AGENT_JAR" /whatap/whatap.agent.jar; \
    ls -al /whatap

# =========================
# 2) jlink로 Slim JRE 생성
# =========================
FROM eclipse-temurin:25-jdk AS jre
WORKDIR /jrebuild

COPY --from=builder /app/target/*.jar ./app.jar

RUN set -eux; \
    DEPS="$(jdeps --ignore-missing-deps --multi-release=25 --print-module-deps app.jar)"; \
    jlink --strip-debug --no-man-pages --no-header-files --compress=2 \
      --add-modules "$DEPS,\
java.desktop,java.management,jdk.management,java.sql,java.naming,\
java.logging,java.security.jgss,jdk.security.auth,java.security.sasl,java.instrument,\
jdk.crypto.ec,jdk.unsupported" \
      --output /opt/jre

# =========================
# 3) Runtime (Distroless)
# =========================
FROM gcr.io/distroless/base-debian12:nonroot
WORKDIR /app

COPY --from=jre        /opt/jre              /opt/jre
COPY --from=builder    /app/target/*.jar     ./app.jar

# ✅ Whatap agent 파일도 런타임에 포함
COPY --from=whatap_agent /whatap /whatap

ENV PATH="/opt/jre/bin:${PATH}"

# ✅ -javaagent는 여기서 기본 탑재 (배포에서 덮어써도 됨)
ENV JAVA_TOOL_OPTIONS="\
-javaagent:/whatap/whatap.agent.jar \
-XX:+UseContainerSupport \
-XX:MaxRAMPercentage=75 \
-XX:+ExitOnOutOfMemoryError \
-XX:+AlwaysActAsServerClassMachine"

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]