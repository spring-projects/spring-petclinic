FROM openjdk:alpine
WORKDIR /tmp
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
EXPOSE 8080
