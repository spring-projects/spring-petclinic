FROM openjdk:17-jdk-slim@sha256:9a37f2c649301b955c2fd31fb180070404689cacba0f77404dd20afb1d7b8d84
ARG app_version=4.0.7
LABEL application_version=${app_version}
WORKDIR /app
COPY target/checkout/target/spring-petclinic-${app_version}.jar /app/java-application.jar
CMD ["java", "-jar", "java-application.jar"]
