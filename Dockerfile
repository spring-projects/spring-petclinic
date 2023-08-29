FROM eclipse-temurin@sha256:039f727ed86402f37524b5d01a129947e2f061d5856901d07d73a454e689bb13 AS builder
ARG VERSION undefined
ENV VERSION=$VERSION
WORKDIR /app
COPY . .
RUN ./gradlew build -x test -x processTestAot -x checkstyleTest -x checkstyleAot -x checkstyleMain -Pversion=$VERSION

FROM eclipse-temurin@sha256:e90e0d654765ab3ae33f5c5155daafa4a907d0d738ce98c3be8f402a8edcee2b
ENV PORT 8080
WORKDIR /app
COPY --from=builder /app/build/libs/*jar .




