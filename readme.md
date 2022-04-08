# Spring PetClinic Sample Application [![Build Status](https://travis-ci.org/spring-projects/spring-petclinic.png?branch=main)](https://travis-ci.org/spring-projects/spring-petclinic/)

Fork of the [Spring Petclinic](https://github.com/spring-projects/spring-petclinic) sample applications used to showcase some of the recent vulnerabilities, namely log4shell and spring4shell. 

## Branches
- [3.1.x](/robinwyss/spring-petclinic/tree/v3.1.x) uses Spring Boot 2.5.7 and is affected by log4shell and spring4shell
- [3.2.x](/robinwyss/spring-petclinic/tree/v3.2.x) uses Spring Boot 2.6.4 and is only affected by spring4shell
- [3.3.x](/robinwyss/spring-petclinic/tree/v3.2.x) uses Spring Boot 2.6.6 and is not affected by either vulnerability

## Running petclinic locally
Petclinic is a [Spring Boot](https://spring.io/guides/gs/spring-boot) application built using [Maven](https://spring.io/guides/gs/maven/). You can build a jar file and run it from the command line:


```
git clone https://github.com/spring-projects/spring-petclinic.git
cd spring-petclinic
./mvnw package
java -jar target/*.jar
```

You can then access petclinic here: http://localhost:8080/

<img width="1042" alt="petclinic-screenshot" src="https://cloud.githubusercontent.com/assets/838318/19727082/2aee6d6c-9b8e-11e6-81fe-e889a5ddfded.png">

Or you can run it from Maven directly using the Spring Boot Maven plugin. If you do this it will pick up changes that you make in the project immediately (changes to Java source files require a compile as well - most people use an IDE for this):

```
./mvnw spring-boot:run
```

> NOTE: Windows users should set `git config core.autocrlf true` to avoid format assertions failing the build (use `--global` to set that flag globally).

