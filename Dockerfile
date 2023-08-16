FROM amazoncorretto:17.0.7-alpine
VOLUME /tmp
ADD target/spring-petclinic-3.1.0.jar app.jar
EXPOSE 80
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]