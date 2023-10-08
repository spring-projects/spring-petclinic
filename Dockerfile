FROM maven:3.9.3-eclipse-temurin-17-alpine as build
VOLUME /app
COPY src /app/src
COPY pom.xml /app/pom.xml
RUN mvn -f /app/pom.xml clean package

FROM gcr.io/distroless/java17-debian11
VOLUME /app
COPY --from=build /app/target/*.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
