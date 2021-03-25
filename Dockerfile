FROM java:8-jdk-alpine
COPY /home/owlleg6/builds/${BUILD_ID} /home/spring-petclinic-2.4.2.jar
CMD ["java","-jar","/home/spring-petclinic-2.4.2.jar"]
