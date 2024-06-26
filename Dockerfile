FROM openjdk:17-jdk-alpine

WORKDIR /app/petclinic

COPY target/spring-petclinic-3.3.0-SNAPSHOT.jar .

EXPOSE 8888


ENTRYPOINT ["java", "-jar", "spring-petclinic-3.3.0-SNAPSHOT.jar"]
