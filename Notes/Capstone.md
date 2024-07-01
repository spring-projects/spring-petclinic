# Code configuration

For capstone project I made some changes in this code base. Mainly in: Dockerfile, Jenkinsfile and build.gradle.

Also I learned how to use MySQL for spring petclinic - it reqiures some enviroment variables:

* MYSQL_USER
* MYSQL_PASSWORD
* MYSQL_ROOT_PASSWORD
* MYSQL_DATABASE
* MYSQL_URL

They are needed for conenction to RDS. One can use it via `docker -e MYSQL_...` or `export MYSQL_...` (for `java -jar` usage).

<hr>

## Pipeline configuration

### Build

* Application is built with Gradle 8.X
* Checkstyle is provided via Gradle plugin

<hr>

### CI/CD

Jenkins server needs to be initilized manually (user, password, plugins, etc.)

For the pipeline to work correctly one needs to setup credentials for Docker Hub and GitHub in Jenkins build server after provisioning infrastructure.

The Github credentials are used to push tags ti repository.

Also one needs to configure repository webhook for build server.

<hr>

### Dockerfile

For image creation basic gradle image is used for build purposes, with addition of distroless layer for application. Image is split into layers according to (at time of creation and my knowlage) current standards, and optimalized for minimal size.

<hr>
