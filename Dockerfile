# syntax=docker/dockerfile:1

# build the jar
FROM --platform=$BUILDPLATFORM eclipse-temurin:17-jdk AS builder
WORKDIR /build

# JFrog coordinates are not secret, so they come in as build args.
# Credentials arrive as BuildKit secrets below and never touch a layer.
ARG JFROG_URL
ARG JFROG_REPO=petclinic-libs-snapshot
ENV JFROG_URL=${JFROG_URL}
ENV JFROG_REPO=${JFROG_REPO}
# the wrapper downloads Maven itself, so route that through JFrog too
ENV MVNW_REPOURL=${JFROG_URL}/artifactory/${JFROG_REPO}

# copy the maven wrapper and pom.xml first so that we can cache the maven dependencies
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN --mount=type=cache,target=/root/.m2 \
    --mount=type=secret,id=jfrog_user,env=JFROG_USER \
    --mount=type=secret,id=jfrog_password,env=JFROG_PASSWORD \
    MVNW_USERNAME="$JFROG_USER" MVNW_PASSWORD="$JFROG_PASSWORD" \
    ./mvnw -B dependency:go-offline

COPY src/ src/
RUN --mount=type=cache,target=/root/.m2 \
    --mount=type=secret,id=jfrog_user,env=JFROG_USER \
    --mount=type=secret,id=jfrog_password,env=JFROG_PASSWORD \
    MVNW_USERNAME="$JFROG_USER" MVNW_PASSWORD="$JFROG_PASSWORD" \
    ./mvnw -B clean package -DskipTests

# copy the jar to a new image and extract it
RUN cp target/*.jar app.jar \
    && java -Djarmode=tools -jar app.jar extract --layers --launcher --destination extracted

# build the custom JRE
FROM eclipse-temurin:17-jdk AS jre-builder
RUN jlink \
      --add-modules java.base,java.compiler,java.desktop,java.instrument,java.logging,java.management,java.naming,java.net.http,java.prefs,java.rmi,java.scripting,java.security.jgss,java.security.sasl,java.sql,java.sql.rowset,java.transaction.xa,java.xml,java.xml.crypto,jdk.crypto.cryptoki,jdk.crypto.ec,jdk.jfr,jdk.management,jdk.net,jdk.unsupported \
      --strip-debug --no-man-pages --no-header-files --compress=2 \
      --output /javaruntime

# build the final image
FROM gcr.io/distroless/base-debian12:nonroot
ENV JAVA_HOME=/opt/java
ENV PATH="${JAVA_HOME}/bin:${PATH}"
COPY --from=jre-builder /javaruntime ${JAVA_HOME}

WORKDIR /app

# copy the extracted jar layers from the builder image
COPY --from=builder --chown=nonroot:nonroot /build/extracted/dependencies/ ./
COPY --from=builder --chown=nonroot:nonroot /build/extracted/spring-boot-loader/ ./
COPY --from=builder --chown=nonroot:nonroot /build/extracted/snapshot-dependencies/ ./
COPY --from=builder --chown=nonroot:nonroot /build/extracted/application/ ./

USER nonroot
EXPOSE 8080
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
