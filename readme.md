# PetClinic Sample Application [![Build Status](https://travis-ci.org/spring-projects/spring-petclinic.png?branch=main)](https://travis-ci.org/spring-projects/spring-petclinic/)

## About
Petclinic is a [Spring Boot](https://spring.io/guides/gs/spring-boot) application built using [Maven](https://spring.io/guides/gs/maven/).

## Prerequisites
The following items should be installed in your system:
* JDK 11 or newer.
* `git` command line tool (https://help.github.com/articles/set-up-git)
* Your preferred IDE
    * IntelliJ IDEA
    * Eclipse with the m2e plugin. Note: when m2e is available, there is an m2 icon in `Help -> About` dialog. If m2e is
      not there, just follow the install process here: https://www.eclipse.org/m2e/
    * [Spring Tools Suite](https://spring.io/tools) (STS)
    * [VS Code](https://code.visualstudio.com)

## Running petclinic locally
You can build a jar file and run it from the command line:

```shell
# Clone the repository (remember to change the URL to your forked one):
git clone git@github.com:cleankod/spring-petclinic.git

# Change working directory to inside the project:
cd spring-petclinic

# Build:
./mvnw package

# Run:
java -jar target/*.jar
```

You can then access the application here: http://localhost:8080/

<img width="1042" alt="petclinic-screenshot" src="https://cloud.githubusercontent.com/assets/838318/19727082/2aee6d6c-9b8e-11e6-81fe-e889a5ddfded.png">

Or you can run it from Maven directly using the Spring Boot Maven plugin. If you do this it will pick up changes that you make in the project immediately
(changes to Java source files require a compilation as well - most people use an IDE for this):

```
./mvnw spring-boot:run
```

## Working with Petclinic in your IDE
### Steps:

1) Inside IntelliJ IDEA, in the main menu, choose `File -> Open` and select the Petclinic's [pom.xml](pom.xml). Click on the `Open` button.

    CSS files are generated from the Maven build. You can either build them on the command line `./mvnw generate-resources` or right click on the
    `spring-petclinic` project then `Maven -> Generates sources and Update Folders`.

    A run configuration named `PetClinicApplication` should have been created for you if you're using a recent Ultimate version. Otherwise, run the application
    by right clicking on the `PetClinicApplication` main class and choosing `Run 'PetClinicApplication'`.

2) Navigate to Petclinic

    Visit [http://localhost:8080](http://localhost:8080) in your browser.
