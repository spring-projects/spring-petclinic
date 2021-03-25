FROM java:8-jdk-alpine
COPY /home/owlleg6/jenkins/workspace/builder-centos/spring-petclinic/target/spring-petclinic-2.4.2.jar /home/spring-petclinic-2.4.2.jar
CMD ["java","-jar","/home/spring-petclinic-2.4.2.jar"]
