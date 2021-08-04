# Spring PetClinic Sample Application [![Build Status](https://travis-ci.org/spring-projects/spring-petclinic.png?branch=main)](https://travis-ci.org/spring-projects/spring-petclinic/)


## Running petclinic using docker image
Petclinic is a [Spring Boot](https://spring.io/guides/gs/spring-boot) application built using [Maven](https://spring.io/guides/gs/maven/). You can build a jar file and run it from the command line:


```
docker pull attaulbari/springclinic:52
docker run -p 8081:8080 attaulbari/springclinic:52
```

Open the browser paste http://localhost:8081/ and you will see the application runinning inside container.


## Build Details

Petclinic app is build using jenkins and has 3 stages as part of the build process.
