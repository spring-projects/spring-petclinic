# The "Simple" GitHub Actions pipeline

This repository makes use of a GitHub actions pipeline. We'll be using a workflow to achieve this. 

## The steps
In order to successfully build, our workflow must 

1. Pull the code from the main branch of the git repo [here](https://github.com/spring-projects/spring-petclinic).
1. Ensure that Java 17 is installed on the GitHub runner.
1. Use the Maven wrapper to build the source.
1. Run the Maven tests for the source
1. Use Maven to check dependencies
1. Package the code into a docker container
1. tag the container with the required name
1. Push the container into the Artifactory Repository



# Using the image
In order to use the image, you will first need docker installed on your local system.

__Authenticate to your container registry with your login__
```console
foo@bar:~$ docker login -u [your-login] my0373.jfrog.io
```
__Note:__
*Please replace ```[your-login]``` with your artifactory login.*


__Pull the container image to your local system__
```console
foo@bar:~$ docker pull my0373.jfrog.io/my0373-docker/spring-petclinic:3.1.0-SNAPSHOT
```

__Run the container image.__ 
```console
foo@bar:~$ docker run -d -p 8080:8080 spring-petclinic:3.1.0-SNAPSHOT
```

__Note:__
*Here I am exposing the site on port 8080. Please change to your requirements.*


The Image can be viewed in  artifactory [here](https://my0373.jfrog.io/ui/repos/tree/General/my0373-docker-local/spring-petclinic).

# Testing the application
Once the container is running, you should be able to connect on port 8080 on the target system.

Assuming this is your local system, open a browser to http://127.0.0.1:8080/.




# Security scan
As part of the build, I've executed an xray scan of the repository and attached the scans in the 

### Code Compilation
The first step of the build process is to build

GitHub link to the repo including

Github Actions workflow files within that repo
Docker file within that repo
readme.md file explaining the work and how to run the project
Bonus Deliverable: XRay Scan Data export (JSON format) for your image
