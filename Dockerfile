# Example of custom Java runtime using jlink in a multi-stage container build
FROM gradle:jdk21 as build

RUN mkdir /app

COPY --chown=gradle:gradle . /app
WORKDIR /app
#skip test, becase testin is another 
RUN gradle build -x test --no-daemon 

FROM openjdk:21-jdk-slim as builder

WORKDIR /app

COPY --from=build /app/build/libs/spring-petclinic-3.4.0.jar /app/spring-petclinic.jar
RUN java -Djarmode=layertools -jar spring-petclinic.jar extract


FROM openjdk:21-jdk-slim 
EXPOSE 8080

RUN mkdir /app

COPY --from=builder app/dependencies/ ./
COPY --from=builder app/spring-boot-loader/ ./
COPY --from=builder app/snapshot-dependencies/ ./
COPY --from=builder app/application/ ./

ENV SPRING_PROFILES_ACTIVE=default

ENTRYPOINT ["java", "-XX:+UnlockExperimentalVMOptions",  "-Djava.security.egd=file:/dev/./urandom", "org.springframework.boot.loader.launch.JarLauncher"]
