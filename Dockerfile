FROM gradle:9-jdk21 AS buildstage
WORKDIR /opt/spring-petclinic/

COPY gradlew .
COPY build.gradle .
COPY settings.gradle .
COPY gradle gradle
COPY src src

RUN ./gradlew clean build

FROM openjdk:21-jdk-slim
COPY --from=buildstage /opt/spring-petclinic/build/libs/gradle-practical-task-1.0.0.jar /opt/spring-petclinic/spring-petclinic.jar
WORKDIR /opt/spring-petclinic/
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "spring-petclinic.jar"]
