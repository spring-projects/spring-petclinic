# Jenkins Practical Task

<hr>

## 1. Install Jenkins
## 2. Make a base setup of Jenkins (User Configuration, Plugin Installation)
## 3. Add a Dockerfile to your repository with spring-petclinic
## 4. Create 2 docker repositories on your own Nexus Repository or Docker Hub called "main" and "mr"
## 5. Add Jenkinsfile and describe the following behavior
*Note: Pipelines shoud be executed in Jenkins agents*

### I. The pipeline for a merge request

#### 1. Checkstyle: Use Maven or Gradle checkstyle plugin to generate a code style report. It should be available as a job artifact.

#### 2. Test (with Maven or Gradle)

#### 3. Build: Build without tests (with Maven or Gradle).

#### 4. Create a docker image: Using your Dockerfile in the spring-petclinic repo, create a docker image with spring-petclinic application, tag it using GIT_COMMIT (short) and push it to the "mr"

### II. The pipeline for the main branch

#### 1. Create a docker image: Build a docker image and push it to the "main" repository
