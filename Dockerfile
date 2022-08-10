FROM anapsix/alpine-java 
LABEL maintainer="amar@gmail.com" 
COPY */*.jar /home
CMD ["java","-jar","/home/spring-petclinic-2.7.0-SNAPSHOT.jar"]
