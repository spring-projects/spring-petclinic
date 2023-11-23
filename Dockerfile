# Copyright The OpenTelemetry Authors
# SPDX-License-Identifier: Apache-2.0

# build: docker build -t petclinic .
# run:   docker run -p 8080:8080 petclinic

FROM gradle:8-jdk17 AS builder

WORKDIR /usr/src/app/

COPY ./ ./
RUN gradle bootJar

# -----------------------------------------------------------------------------

FROM gcr.io/distroless/java17-debian11

WORKDIR /usr/src/app/

COPY --from=builder /usr/src/app/build/libs/spring-petclinic-*.jar ./spring-petclinic.jar

ENTRYPOINT [ "java", "-jar", "spring-petclinic.jar" ]
