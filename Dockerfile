
FROM anapsix/alpine-java
LABEL maintainer="amar@gmail.com"
COPY /target/spring-petclinic-1.5.1.jar spring-petclinic-1.5.1.jar
CMD ["java","-jar","/usr/share/spring-petclinic-1.5.1.jarspring-petclinic-1.5.1.jar"]
