FROM openjdk:8-jre-alpine3.9
COPY target/*.jar /home/spring-clinic.jar
CMD ["java","-jar","/home/spring-clinic.jar"]
