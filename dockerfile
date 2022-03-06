FROM anapsix/alpine-java
LABEL maintainer="luisreyes@mgs.ecotec.edu.ec"
COPY /target/spring-petclinic-2.6.0-SNAPSHOT.jar /home/spring-petclinic-2.6.0.jar
CMD ["java","-jar","/home/spring-petclinic-2.6.0.jar"]
