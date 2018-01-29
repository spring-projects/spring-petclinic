FROM openjdk:8-alpine

COPY target/spring-petclinic-2.0.0.jar /opt/spring-petclinic.jar

EXPOSE 8080

CMD java -Djava.security.egd=file:/dev/./urandom -jar /opt/spring-petclinic.jar