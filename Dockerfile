FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD ./target/spring-petclinic-2.4.0.BUILD-SNAPSHOT.jar app.jar
ENV JAVA_OPTS=""
ENTRYPOINT ["java","-jar","/app.jar"]




