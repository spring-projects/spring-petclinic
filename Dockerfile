FROM openjdk:21-jdk

LABEL project="petclinic"

LABEL author="bhanu"

EXPOSE 8080
ARG artifact=target/*.jar

COPY ${artifact} /spring-petclinic.jar

ENTRYPOINT ["java", "-jar", "/spring-petclinic.jar"]
