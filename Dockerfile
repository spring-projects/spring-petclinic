FROM openjdk:20-ea-17-jdk
VOLUME /tmp

COPY target/spring-petclinic-3.0.0-SNAPSHOT.jar spring-petclinic-3.0.0-SNAPSHOT.jar

RUN useradd -d /home/appuser -m -s /bin/bash appuser
USER appuser

HEALTHCHECK --interval=5m --timeout=3s CMD curl -f http://localhost:8080/ || exit 1

ENTRYPOINT ["java","-jar","/spring-petclinic-3.0.0-SNAPSHOT.jar"]
