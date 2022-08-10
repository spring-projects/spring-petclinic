FROM anapsix/alpine-java 
LABEL maintainer="amar@gmail.com" 
COPY /workspace/pipeline/target/spring-petclinic-2.7.0-SNAPSHOT.jar /home
CMD ["java","-jar","/home/spring-petclinic-2.7.0-SNAPSHOT.jar"]
