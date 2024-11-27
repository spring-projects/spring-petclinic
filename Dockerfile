FROM maven:3.8.7-openjdk-18-slim AS build

RUN mkdir /app
COPY . /app
WORKDIR /app
RUN mvn package

# Minimal rintime image - only JRE
# FROM gcr.io/distroless/java21-debian12 AS runtime
# COPY --from=build /app/target/*.jar /app.jar
ENTRYPOINT [ "java" ]
CMD [ "-jar", "-Dspring.profiles.active=postgres", "/app.jar" ]
