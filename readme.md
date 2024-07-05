# Sprint-petclinic codebase

This repository contains codebase for capstone project.

For capstone project I made some changes in this code base. Mainly in: Dockerfile, Jenkinsfile and build.gradle.

Also I learned how to use MySQL for spring petclinic - it reqiures some enviroment variables:

* MYSQL_USER
* MYSQL_PASSWORD
* MYSQL_ROOT_PASSWORD
* MYSQL_DATABASE
* MYSQL_URL

They are needed for conenction to RDS. One can use it via `docker -e MYSQL_...` or `export MYSQL_...` (for `java -jar` usage).

<hr>

## Overview

### Build

* Application is built with Gradle 8.X
* Checkstyle is provided via Gradle plugin

### CI/CD

Jenkins server can be used in two ways:

* As agent
* As separete build server

Each will change a bit in configuration.

### Dockerfile

Dockerfile is provided, also compose file for testing is present. Additionally, there is compose file with provided sample connection string - one needs to change it accoring to RDS endpoint and user data.

<hr>

## Explanation of components

### CI/CD

Jenkins server can be configured in two ways: agent, separete build server.

As default Jenkins is configured as agent for main Controller and needs to be initilized manually (user, password, plugins, connection, etc.)

Agent usage:

* In Controller add agent
* Add agent via commend given from Controller.
* Create multibranch pipeline job in Controller with GitHub project and SCM pipeline.
* Ensure all needed credentials are added in Controller (DockerHub, GitHub, AWS, etc.) - check spring-petclinic-infrastructure README
* Configure repository webhook for server

Separete server usage:

* For the pipeline to work correctly one needs to setup credentials for Docker Hub and GitHub in Jenkins server
* Change `agent` in Jenkinsfile to `any`
* Configure repository webhook for server

### Dockerfile

For image creation basic Gradle image is used for build purposes, with addition of distroless layer for application. Image is split into layers according to (at time of creation and my knowlage) current standards, and optimalized for minimal size.

Two compose files are present - one for testing connection string, other for providing container that connects to RDS.
