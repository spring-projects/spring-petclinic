FROM ubuntu:latest
MAINTAINER evgenyidf <evgenyidf@gmail.com>

RUN apt-get update --fix-missing && apt-get install -y default-jre

RUN mkdir -p /var/spring-petclinic/jars

WORKDIR /var/spring-petclinic
COPY ../target/spring-petclinic*.jar /var/spring-petclinic/spring-petclinic.jar
COPY ../target/dependency-jars/*.jar /var/spring-petclinic/jars

RUN ls -la /var/spring-petclinic/spring-petclinic-2.0.0.BUILD-SNAPSHOT.jar

EXPOSE 8080

#ENTRYPOINT ["java -cp /var/spring-petclinic.jar"]