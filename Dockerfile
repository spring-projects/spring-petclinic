FROM java:8-jdk-alpine
WORKDIR /home/owlleg6/builds/
ADD ./target/*.jar spring-petclinic.jar
EXPOSE 8080
CMD ["java","-jar","/home/petclinic.jar"]
