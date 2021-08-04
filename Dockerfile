FROM anapsix/alpine-java 
COPY /target/spring-petclinic-2.4.5.jar /home/spring-petclinic-2.4.5.jar
CMD ["java","-jar","/home/spring-petclinic-2.4.5.jar"]
