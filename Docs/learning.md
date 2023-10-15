# The "more complex" GitHub Actions pipeline

This repository makes use of multiple jobs withing a GitHub actions pipeline. We'll be using a workflow to achieve this.

This workflow runs the source build, test and dependency validation job. It then runs the container build jobs in parallel. 

## The trigger
The workflow is configured to run when the "main" branch is pushed, or when a PR for the "main" branch is raised.
  
## The steps
In order to successfully build, our workflow must 

### "source-build" job

1. Pull the code from the main branch of the git repo [here](https://github.com/spring-projects/spring-petclinic).
1. Ensure that Java 17 is installed on the GitHub runner.
1. Use the Maven wrapper to build the source.
1. Run the Maven tests for the source
1. Use Maven to check dependencies

### "container-build" job
1. Pull the code from the main branch of the git repo [here](https://github.com/spring-projects/spring-petclinic).
1. Ensure that Java 17 is installed on the GitHub runner.
1. Package the code into a docker container
1. Tag the container with the required name
1. Store the container as a binary artifact in the GitHub action.

### "publish-build" job

1. Retrieve the container as a binary artifact from GitHub.
1. Restore the container from a tarfile.
1. Setup the JFrog CLI tool
1. Use the jfrog scanner to scan the image for known vulnerabilities.
1. Push the tested, scanned image to the Artifactory repository.




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
As part of the build, I've executed an xray scan of the repository and attached the scans in the Scan directory of the repository  [here](https://github.com/my0373/spring-petclinic/tree/main/Scan).


