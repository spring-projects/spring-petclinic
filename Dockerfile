FROM java:8
WORKDIR /
ADD target/spring-petclinic-2.0.0.BUILD-SNAPSHOT.jar .
EXPOSE 8080
CMD java - jar spring-petclinic-2.0.0.BUILD-SNAPSHOT.jar