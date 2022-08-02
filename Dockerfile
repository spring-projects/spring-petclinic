FROM openjdk:11-slim
ARG VERSION=2.7.0-SNAPSHOT
WORKDIR /var/spring/petclinic
EXPOSE 8080
COPY target/petclinic-${VERSION}.jar /var/spring/petclinic/petclinic.jar
ENTRYPOINT ["java","-jar","/var/spring/petclinic/petclinic.jar"]