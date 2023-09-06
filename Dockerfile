FROM ubuntu:22.04
RUN git clone https://github.com/spring-projects/spring-petclinic.git
RUN cd spring-petclinic
RUN ./mvnw package
CMD ["java" "-jar" "target/*.jar"]