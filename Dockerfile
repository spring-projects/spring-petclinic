FROM openjdk:8-jdk-alpine
EXPOSE 8080
ARG JAR_FILE=target/spring-petclinic-2.4.2.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
