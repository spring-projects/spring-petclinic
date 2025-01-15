# First stage: Build the application with Maven
FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Second stage: Download New Relic Java APM agent
FROM alpine:3.14 AS newrelic
WORKDIR /newrelic
RUN wget https://download.newrelic.com/newrelic/java-agent/newrelic-agent/current/newrelic-java.zip && \
    unzip newrelic-java.zip

# Third stage: Copy artifacts to the distroless JRE image and run them
FROM gcr.io/distroless/java17-debian11
WORKDIR /app
USER root
COPY --from=build /app/target/spring-petclinic.jar /app/spring-petclinic.jar
COPY --from=newrelic /newrelic/newrelic/ /newrelic/
ENV NEW_RELIC_APP_NAME=petclinic
ENTRYPOINT ["java", "-javaagent:/newrelic/newrelic.jar", "-jar", "/app/spring-petclinic.jar"]