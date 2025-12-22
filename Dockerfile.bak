# =========================
# 1) Build (Maven + JDK 25)
# =========================
FROM maven:3.9-eclipse-temurin-25 AS builder
WORKDIR /app

# 의존성 캐시
COPY pom.xml ./
RUN --mount=type=cache,target=/root/.m2 \
    mvn -q -DskipTests dependency:go-offline

# 전체 소스 복사
COPY . .

# 패키징 (테스트 스킵)
RUN --mount=type=cache,target=/root/.m2 \
    mvn -q -DskipTests package



# =========================
# 2) jlink로 Slim JRE 생성
# =========================
FROM eclipse-temurin:25-jdk AS jre
WORKDIR /jrebuild

# Fat JAR 복사
COPY --from=builder /app/target/*.jar ./app.jar

# jdeps + jlink : Spring Boot + Tomcat용 공통 모듈 세트 포함
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

# jlink로 만든 JRE + 앱 복사
COPY --from=jre     /opt/jre                /opt/jre
COPY --from=builder /app/target/*.jar       ./app.jar

# java 실행 경로
ENV PATH="/opt/jre/bin:${PATH}"

# JVM 옵션
ENV JAVA_TOOL_OPTIONS="\
-XX:+UseContainerSupport \
-XX:MaxRAMPercentage=75 \
-XX:+ExitOnOutOfMemoryError \
-XX:+AlwaysActAsServerClassMachine"

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]

