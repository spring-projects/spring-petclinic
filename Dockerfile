# syntax=docker/dockerfile:1
FROM amazoncorretto:17 as base

# Install OS packages
#RUN apk --no-cache add curl
WORKDIR /app
expose 8080

FROM amazoncorretto:17 as build
WORKDIR /src
COPY src ./src 
COPY gradle ./gradle
COPY build.gradle gradlew settings.gradle ./
RUN --mount=type=cache,target=/root/.gradle \
      ./gradlew build --console=plain --info --no-daemon --no-watch-fs

FROM base AS final
WORKDIR /app
COPY --from=build /src/build/libs/spring-petclinic-2.6.0.jar .
COPY ["newrelic/", "./newrelic"]

COPY --chmod=0755 entrypoint.sh /
COPY --chmod=0755 tester.sh /app

# Set env vars
# ENV NEW_RELIC_CONFIG_FILE=/app/newrelic/newrelic.yml

ENTRYPOINT ["/entrypoint.sh"]
