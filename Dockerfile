FROM ubuntu:22.04
RUN git clone https://github.com/Cloud-and-devops-notes/spring-petclinic-jenkins.git
RUN cd spring-petclinic
RUN ./mvnw package
CMD ["java" "-jar" "target/*.jar"]