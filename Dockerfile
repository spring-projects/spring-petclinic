FROM openjdk:11-jdk

COPY target/spring-petclinic-2.4.2.jar /opt/spring-petclinic.jar

EXPOSE 8080
CMD ["java", "-jar", "/opt/spring-petclinic.jar"]
