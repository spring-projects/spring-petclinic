FROM openjdk:17-jre-alpine
COPY /target/*.jar /home/app.jar
CMD ["java","-jar","/home/app.jar"]
