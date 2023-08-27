FROM ubuntu20.04
RUN apt update && \
    apt install openjdk-11-jdk -y && \
    git clone https://github.com/lahari104/spring-petclinic.git && \
    cd spring-petclinic && \
    ./mvn package 
EXPOSE 8080
CMD [ "java", "-jar", "spring-petclinic" ]