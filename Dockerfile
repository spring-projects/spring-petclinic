# Arguments:
# project = project name
# artifactId
# VERSION

# Build stage
FROM maven:3.5-jdk-8-alpine as build
ARG project
WORKDIR /app
COPY . /app
RUN mvn clean package -DskipTests

# Run stage
FROM openjdk:8-jre-alpine
ARG ARTIFACT_ID=spring-petclinic
ARG VERSION=2.5.0-SNAPSHOT
ARG PORT=8080
ENV ARTIFACT=${ARTIFACT_ID}-${VERSION}.jar
WORKDIR /app
COPY --from=build /app/target/${ARTIFACT} /app
EXPOSE $PORT
ENTRYPOINT ["sh", "-c"]
CMD ["java -jar ${ARTIFACT} --server.port=${PORT}"]