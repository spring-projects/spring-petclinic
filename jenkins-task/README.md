# Jenkins Practical Task


<hr>

## 1. Install Jenkins

* Pull Jenkins image
![Pull Image](1.%20installation/1.png)
* Run Jenkins container
![Run](1.%20installation/2.png)
![Run](1.%20installation/3.png)
* See container logs to get the password
![Run](1.%20installation/4.png)
![Run](1.%20installation/5.png)


## 2. Make a base setup of Jenkins (User Configuration, Plugin Installation)

* User Configuration
![Run](2.%20User%20Configuration/1.png)

## 3. Add a Dockerfile to your repository with spring-petclinic

* See commit history

## 4. Create 2 docker repositories on your own Nexus Repository or Docker Hub called "main" and "mr"

![Repositories](4.%20Docker%20Hub/1.png)

## 5. Add Jenkinsfile and describe the following behavior
*Note: Pipelines shoud be executed in Jenkins agents*

### I. The pipeline for a merge request

#### 1. Checkstyle: Use Maven or Gradle checkstyle plugin to generate a code style report. It should be available as a job artifact.

#### 2. Test (with Maven or Gradle)

#### 3. Build: Build without tests (with Maven or Gradle).

#### 4. Create a docker image: Using your Dockerfile in the spring-petclinic repo, create a docker image with spring-petclinic application, tag it using GIT_COMMIT (short) and push it to the "mr"

### II. The pipeline for the main branch

#### 1. Create a docker image: Build a docker image and push it to the "main" repository

* Testing webhooks
* Testing webhooks 2
* Testing webhooks 7