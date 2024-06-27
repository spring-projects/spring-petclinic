# Spring PetClinic Sample Application 

## Prerequisites
- Read and understand the PetClinic application documentation: [ReadME.MD](readme-original.md)

## Objective
Develop a DevOps pipeline to automate tasks such as code compile, unit testing, creation of container, and upload of artifacts to a repository. This will streamline the software development process.

Note: This process with not deploy to the envionrmnet platform. 

### Changelog
- The original 'readme.md' file has been renamed to 'readme-orignial.md'.
- Add new files
    - readme.md
    - Dockerfile
    - Jenkinsfile

## DevOps steps
### Video walkthrough
[![Walk through demo](https://img.youtube.com/vi/zgiaPIp-ZZA/0.jpg)](https://www.youtube.com/watch?v=zgiaPIp-ZZA)
### Assumption
My assumption is that the user has the necessary infrastucture in place, including Java v17+, Maven v3.8+, Docker v25+, and a Jenkins v2.445+ server, to implement an automated pipeline for the PetClinic application.
`````
docker pull psazuse.jfrog.io/krishnam-docker-virtual/myjenkins:latest

docker pull psazuse.jfrog.io/krishnam-docker-virtual/krishnamanchikalapudi/myjenkins:latest
`````

### Pipeline
<img src="./cipipeline.svg">

The Jenkins pipeline script performs the following stages:
#### Stage: Code
The stage 'Code' contains the following sequential sub-stages:
##### clean
This sub-stage remove any exisiting petclinc directory
`````
rm -rf spring-petclinic
`````
##### clone
This sub-stage clones the GitHub repository
`````
git branch: 'main', url: 'https://github.com/krishnamanchikalapudi/spring-petclinic.git'
`````
##### compile
This sub-stage compiles and skips tests the project using Apache Maven
`````
mvn clean install -DskipTests=true
`````
#### Stage: Tests
The stage 'Tests' contains parallel sub-stages:
##### unitTest
This sub-stage runs unit tests using Apache Maven command and generates a surefire html report at the ${project}/${buildNumber}/target/site/surefire-report.html. Note: If the execution is not completed within ten (10) minutes, the stage is failed.
`````
mvn test surefire-report:report
`````
##### checkStyle
This sub-stage runs the Checkstyle checks using Apache Maven command and generates a checkstyle html report at the ${project}/${buildNumber}/target/site/checkstyle.html. Note: If the execution is not completed within two (2) minutes, the stage is failed.
`````
mvn checkstyle:checkstyle
`````
##### codeCoverage
This sub-stage generates Jacoco code coverage reports using Apache Maven command and generates a Jacoco html report at the ${project}/${buildNumber}/target/site/jacoco/index.html. Note: If the execution is not completed within two (2)  minutes, the stage is failed.
`````
mvn jacoco:report
`````
#### Stage: Container
The stage 'Docker' contains the following sub-stages:
##### build
This sub-stage builds the Docker image using the Dockerfile. The image is tagged with the project name and the Jenkins build ID.
`````
docker image build -f Dockerfile -t mypetclinic:${env.BUILD_ID} .
`````
##### tag
This sub-stage lists Docker containers and images, tags the built image with project name and latest tags, and tags the built image with the project name and build ID.
`````
docker tag mypetclinic:${env.BUILD_ID} krishnamanchikalapudi/mypetclinic:${env.BUILD_ID}
docker tag mypetclinic:${env.BUILD_ID} krishnamanchikalapudi/mypetclinic:latest
`````
##### publish
This sub-stage pushes the Docker images to the Docker registry using Docker login credentials stored in Jenkins credentials. It pushes both the latest and build ID tagged images.
`````
docker push krishnamanchikalapudi/mypetclinic:${env.BUILD_ID}
docker push krishnamanchikalapudi/mypetclinic:latest
`````
##### clean
This sub-stage performs cleanup tasks such as pruning unused Docker images and containers, and removing the built image from the local system.
`````
docker system prune -f --filter until=1h
docker rmi -f mypetclinic
`````

## Repository
- The container images from the Jenkin pipeline was uploaded to the [Docker repository](https://hub.docker.com/orgs/krishnamanchikalapudi/repositories)

## Docker image execution
A few options are available to execute the Docker image [krishnamanchikalapudi/mypetclinic:latest](https://hub.docker.com/r/krishnamanchikalapudi/mypetclinic/tags)
### Option 1
- Prerequisite
    - Device operating system: Mac or Linux
    - Docker version 25+
- Download [mypetclinic.sh](https://github.com/krishnamanchikalapudi/spring-petclinic/blob/main/mypetclinic.sh) from the GitHub repo# [https://github.com/krishnamanchikalapudi/spring-petclinic](https://github.com/krishnamanchikalapudi/spring-petclinic)
- Execute the script with argument
`````
./mypetclinic.sh start
`````
- Stop the image
`````
./mypetclinic.sh stop
`````
### Option 2
- Prerequisite
    - Device operating system: Mac, Linux, or Windows
    - Docker version 25+
- Download the docker image
`````
docker pull krishnamanchikalapudi/mypetclinic:latest
`````
`````
docker pull psazuse.jfrog.io/krishnam-docker-virtual/krishnamanchikalapudi/mypetclinic:latest
`````

- Execute the container
`````
docker run -d --name mypetclinic -p 7080:8080 krishnamanchikalapudi/mypetclinic:latest
`````
    - NOTE: Update port '7080', if any other service is currently running.
- Open browser with url: [http://localhost:7080](http://localhost:7080)
- Stop the container
`````
docker stop $(docker ps -q --filter ancestor=mypetclinic)
docker ps --filter name=mypetclinic --filter status=running -aq | xargs docker stop
`````

## Jenkins Setup
### Prerequisite
- Follow instruction to secure [DockerHub token] at the [Jenkins credentials](https://www.jenkins.io/doc/book/using/using-credentials/) 
### New project 
- Follow instructions to setup new project using [Jenkinsfile with SCM](https://www.jenkins.io/doc/book/pipeline/jenkinsfile/)

## Learnings
### Access issue on my device
#### error
The Jenkins pipeline stage docker/build throws an error when it tries to make mypetclinic image as below.
`````
ERROR: permission denied while trying to connect to the Docker daemon socket at unix:///var/run/docker.sock: Get "%2Fvar%2Frun%2Fdocker.sock/_ping": dial unix /var/run/docker.sock: connect: permission denied
`````
#### solution
My device is running with user 'krishna' and Jenkins service is running with the user 'jenkins'. Therefore, I need to grant the user 'jenkins' access to docker.sock and command follows
`````
sudo setfacl -R -m user:jenkins:rwx /var/run/docker.sock
`````

### LAST UMCOMMIT
`````
git reset --hard HEAD~1
git push origin -f
`````

## License
The Spring PetClinic sample application is released under version 2.0 of the [Apache License](https://www.apache.org/licenses/LICENSE-2.0).
