FROM anapsix/alpine-java 
LABEL maintainer="amar@gmail.com" 
COPY target/*.jar /home
CMD ["java","-jar","/home/spring-petclinic-2.7.0-SNAPSHOT.jar"]
