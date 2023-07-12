FROM anapsix/alpine-java 
LABEL maintainer="shanem@liatrio.com"
COPY /target/spring-petclinic-3.1.0-SNAPSHOT.jar /home/spring-petclinic-3.1.0.jar 
CMD ["java","-jar","/home/spring-petclinic-3.1.0.jar"]
