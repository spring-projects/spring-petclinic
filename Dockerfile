FROM openjdk:17-jdk-slim
COPY target/spring-petclinic-*.jar /app/spring-petclinic.jar
ENTRYPOINT ["java", "-jar", "/app/spring-petclinic.jar"]

