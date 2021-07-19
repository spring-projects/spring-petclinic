FROM openjdk:8u292-jre-slim
EXPOSE 8080
ARG JAR=spring-petclinic-2.4.5.jar
COPY target/$JAR /petclinic.jar
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=mysql", "/petclinic.jar"]
