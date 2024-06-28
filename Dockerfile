FROM maven:3.9.6-amazoncorretto-17-al2023@sha256:665ce50a354231b6c2e713f0d960814bdbe498adf143f7f477778c1a18c285a7 AS build

RUN mkdir /project
COPY . /project
WORKDIR /project

RUN mvn clean package -DskipTests -Dcheckstyle.skip



FROM gcr.io/distroless/java17

EXPOSE 8080
EXPOSE 12345
WORKDIR /app

COPY --from=build /project/target/spring-petclinic-*.jar /app/java-application.jar
COPY --from=build /project/metrics /app/metrics

# ENTRYPOINT [ "java", "-Dspring.profiles.active=postgres", "-jar", "java-application.jar"]
# ENTRYPOINT [ "java", "-javaagent:/app/metrics/jmx_prometheus_javaagent-1.0.1.jar=12345:/app/metrics/config.yaml", "-jar", "java-application.jar"]
ENTRYPOINT [ "java", " -Dspring.profiles.active=mysql", "-jar", "java-application.jar"]
