FROM ubuntu:latest
MAINTAINER evgenyidf <evgenyidf@gmail.com>

RUN apt-get update --fix-missing && apt-get install -y default-jre

RUN mkdir /var/spring-petclinic
COPY target/spring-petclinic*.jar /var/spring-petclinic/spring-petclinic.jar

EXPOSE 8080

VOLUME [".", "/"]

CMD ["java -jar /var/spring-petclinic/spring-petclinic.jar"]