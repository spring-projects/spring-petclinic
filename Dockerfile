FROM java:8-jdk-alpine
WORKDIR /home/owlleg6/builds/
ADD /home/owlleg6/builds/petclinic.jar /home/spring-petclinic.jar
CMD ["java","-jar","/home/petclinic.jar"]
