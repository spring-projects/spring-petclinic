FROM openjdk:16-alpine3.13 as base

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline
COPY src ./src

FROM base as test
RUN ["./mvnw", "test"]

FROM base as build
RUN ./mvnw package

FROM openjdk:11-jre-slim as production
ENV PORT="8080"
EXPOSE ${PORT}

COPY --from=build /app/target/spring-petclinic-*.jar /spring-petclinic.jar

CMD ["java", "-Djava.security.egd=file:/dev/./urandom", "-Dspring.profiles.active=mysql", "-Dserver.port=${PORT}", "-jar", "/spring-petclinic.jar"]
