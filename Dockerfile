FROM openjdk:21-jdk-slim
COPY /build/libs/*.jar /opt/spring-petclinic/*.jar
WORKDIR /opt/spring-petclinic/
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "*.jar"]