FROM openjdk:20-ea-17-jdk
COPY target/spring-petclinic-3.0.0-SNAPSHOT.jar spring-petclinic-3.0.0-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/spring-petclinic-3.0.0-SNAPSHOT.jar"]
