FROM anapsix/alpine-java 
LABEL maintainer="amar@gmail.com" 
RUN ["chmod 777 /target/spring-petclinic-2.7.0-SNAPSHOT.jar"]
COPY /target/spring-petclinic-2.7.0-SNAPSHOT.jar /home/spring-petclinic-2.7.0-SNAPSHOT.jar
CMD ["java","-jar","/home/spring-petclinic-2.7.0-SNAPSHOT.jar"]
