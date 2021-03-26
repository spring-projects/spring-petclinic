FROM java:8-jdk-alpine
WORKDIR /home/owlleg6/builds/
ADD ./target/*.jar spring-petclinic.jar
CMD ["java","-jar","/home/petclinic.jar"]
