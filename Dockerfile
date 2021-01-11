FROM maven:3.6.3-jdk-8 AS build-env
WORKDIR /app

COPY pom.xml ./
RUN mvn dependency:go-offline

COPY . ./
RUN mvn validate compile test
RUN mvn package

FROM openjdk:8-jre-alpine
WORKDIR /app
COPY --from=build-env /app/target/spring-petclinic-2.4.0.BUILD-SNAPSHOT.jar ./spring-petclinic.jar
COPY --from=build-env /app/target/spring-petclinic-2.4.0.BUILD-SNAPSHOT.jar ./spring-petclinic-2.4.0.BUILD-${env.BUILD_ID}.jar
RUN apk update && apk --no-cache add curl
RUN curl -X PUT -u jfroguser:AdminPassword1 ./spring-petclinic-2.4.0.BUILD-${env.BUILD_ID}.jar "https://petclinic.jfrog.io/artifactory/spring-petclinic/spring-petclinic-2.4.0.BUILD-${BUILD_NUMBER}.jar"
CMD ["java", "-jar", "/app/spring-petclinic.jar"]
EXPOSE 8080
