ENV WORKDIR /app
ENV PORT 8080
ENV VERSION undefined

FROM eclipse-temurin@sha256:039f727ed86402f37524b5d01a129947e2f061d5856901d07d73a454e689bb13 AS builder
WORKDIR $WORKDIR
COPY . .
RUN ./gradlew build -x test -Pversion=$VERSION

FROM eclipse-temurin@sha256:e90e0d654765ab3ae33f5c5155daafa4a907d0d738ce98c3be8f402a8edcee2b
WORKDIR $WORKDIR
COPY --from=builder $APP_HOME/build/libs/*jar .
