FROM gradle:jdk22-jammy as builder
COPY . /home/gradle/source
WORKDIR /home/gradle/source
RUN gradle build --stacktrace --no-daemon

FROM openjdk:24-slim-bullseye
COPY --from=builder /home/gradle/source/build/libs/spring-petclinic-3.3.0.jar /app/
# COPY --from=builder ./build/libs/spring-petclinic-3.3.0-plain.jar /app/

WORKDIR /app
ENTRYPOINT ["java", "-jar", "spring-petclinic-3.3.0.jar"]
