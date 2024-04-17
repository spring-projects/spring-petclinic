FROM maven:3.9.6-amazoncorretto-17-al2023@sha256:665ce50a354231b6c2e713f0d960814bdbe498adf143f7f477778c1a18c285a7 AS build
RUN mkdir /project
COPY . /project
WORKDIR /project
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17.0.10_7-jre-jammy@sha256:1b646daef966395c93995e73347d4c7c726c9ddba8695e984cd8dcf5d8b5b253
ARG app_version=4.0.8-SNAPSHOT
LABEL application_version=${app_version}
RUN addgroup --system appuser && adduser --shell /bin/false --no-create-home --ingroup appuser appuser
WORKDIR /app
RUN chown -R appuser:appuser /app
COPY --from=build /project/target/spring-petclinic-${app_version}.jar /app/java-application.jar
EXPOSE 8080
USER appuser
CMD "java" "-jar" "java-application.jar"
