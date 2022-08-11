
# Docker Build Stage
FROM anapsix/alpine-java


# Copy folder in docker
WORKDIR /opt/app

COPY ./ /opt/app
RUN mvn clean install 


# Run spring boot in Docker
FROM openjdk:8-jdk-alpine

COPY --from=build /opt/app/target/*.jar app.jar

ENV PORT 8081
EXPOSE $PORT

ENTRYPOINT ["java","-jar","-Xmx1024M","-Dserver.port=${PORT}","app.jar"]

