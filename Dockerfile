FROM openjdk:14-jdk
COPY target/spring-petclinic-2.3.0.BUILD-SNAPSHOT.jar /pet.jar
ENTRYPOINT ["java", "-jar", "/pet.jar"]
