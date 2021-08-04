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

All docker based enviornments are defined in first block of the pipeline to utilize these in order to access dockerhub and credentials while building and pushing image.

The build job is running locally on docker based jenkins master and using maven 3.8.1 as base version.

## Stages

Build stage is the first and responsible to compile the code along with copying settings.xml file from the artifactory to resolve the dependencies.

```
mvn clean install 
-Dcheckstyle.skip ### Added to skip the checkstyle errors.
```

## upload to artifactory

This stage is responsible to upload the .jar file to the artifactory. Make sure to install Jfrog Artifactory plugin in order to use this functionality.

## Docker Build

This step is nothing but compling the docker image based of the Dockerfile. Like Artifactory plugin, make sure to use Docker plugin to use docker based commands in the pipeline.

# Deploy Image to dockerhub

This stage will push out the image to the dockerhub repo which is attaulbari/springclinic. Everytime time an image will be pushed out with the build number to have better tracking.

# Image Clean up

This stage is responsible for removing the image after running the job and avoid space to be filled up on the agent/node.

# Post

post section of the pipeline is to allow junit access .xml files

At the end, workspace get cleaned after every job.




