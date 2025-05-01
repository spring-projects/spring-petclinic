FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

COPY target/spring-petclinic-*.jar /app/spring-petclinic.jar

CMD ["java", "-jar", "spring-petclinic.jar"]