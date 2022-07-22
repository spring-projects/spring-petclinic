FROM azul/zulu-openjdk:11 as base

# Install OS packages
RUN apt-get update && \
    apt-get -y install curl && \
    rm -rf /var/lib/apt/lists/*
WORKDIR /app
expose 8080

FROM azul/zulu-openjdk:11 as build
WORKDIR /src
COPY src ./src 
COPY gradle ./gradle
COPY build.gradle gradlew settings.gradle ./
RUN ./gradlew build

FROM base AS final
WORKDIR /app
COPY --from=build /src/build/libs/spring-petclinic-2.6.0.jar .
COPY ["newrelic/", "./newrelic"]

COPY --chmod=0755 entrypoint.sh /
COPY --chmod=0755 tester.sh /app

# Set env vars
# ENV NEW_RELIC_CONFIG_FILE=/app/newrelic/newrelic.yml

ENTRYPOINT ["/entrypoint.sh"]
