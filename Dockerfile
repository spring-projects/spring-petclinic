FROM openjdk:17
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
