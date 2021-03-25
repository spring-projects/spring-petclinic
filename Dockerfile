FROM java:8-jdk-alpine
WORKDIR /home/owlleg6/builds/
COPY petclinic.jar /home/spring-petclinic.jar
CMD ["java","-jar","/home/petclinic.jar"]
