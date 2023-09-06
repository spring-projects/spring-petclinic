FROM ubuntu:22.04
RUN apt update && apt install git -y
RUN git clone https://github.com/Cloud-and-devops-notes/spring-petclinic-jenkins.git
RUN cd spring-petclinic-jenkins
RUN ./mvnw package
CMD ["java" "-jar" "target/*.jar"]