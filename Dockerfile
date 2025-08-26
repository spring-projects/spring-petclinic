FROM openjdk:24-jdk-slim
COPY /build/libs/gradle-practical-task-1.0.0.jar /opt/spring-petclinic/spring-petclinic.jar
WORKDIR /opt/spring-petclinic/
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "spring-petclinic.jar"]