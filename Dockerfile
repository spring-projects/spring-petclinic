FROM ubuntu:22.04
RUN apt update
RUN apt install openjdk-17-jdk wget -y
RUN wget https://springpetcli.s3.ap-southeast-2.amazonaws.com/spring-petclinic-3.0.0-SNAPSHOT.jar
EXPOSE 8080
CMD ["java", "-jar", "/spring-petclinic-3.0.0-SNAPSHOT.jar"]
