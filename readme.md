# DevOps excercises

This repository contains sample spring-boot app - [spring-petclinic](https://github.com/spring-projects/spring-petclinic). It is a starting point for excercises with:

* Gradle/Maven - build tools
* Nexus - artifact management, private repositories
* Docker - containerisation
* Jenkins - CI + CD
* AWS - cloud

<hr>

## Build tools

1. Getting familiar with wrappers
2. Setting up with build.gradle/pom.xml
3. Getting familiar with test/build/package/checkstyle/install/cleanup processes
4. Adding source control management (SCM)
5. Versioning with release plugin
6. Plugin management
7. Custom jobs (Gradle)
8. Adding custom repositories

## Nexus

1. Setting up proxy and private repositories
2. Releasing artifacts to private repository
3. Integration with maven via Nexus Repository Maven Plugin

## Docker

1. Containers vs VM
2. How container works
3. Dockerfile and its (best practices)[https://docs.docker.com/develop/develop-images/dockerfile_best-practices/]
4. Multi-stage builds
5. Usage in (local development)[https://docs.docker.com/language/java/develop/]
6. (How to make images smaller)[https://learnk8s.io/blog/smaller-docker-images] - distroless/slim base images + multi-stage + less layers
7. (Vulnerability scanning)[https://docs.docker.com/scout/]
8. Basics of docker compose

## Jenkins

1. Core principals of CI/CD
2. Credentials management
3. Plugins
4. Distributed builds using agents
5. Integrate source code management, build tools, and test reports in Jenkins. (>Tools)
6. Manage builds
7. Enviroment variables and parametrised builds
8. Integration with docker
9. Creating basic pipeline

## AWS

1. Console + CLI operations
2. Credential management for CLI
3. Creation and usage of basic components: S3, EC2, VPC, ECR, Security Groups, IAM.
4. Some automation with bash scripts

Note: run scripts using `source` command, eg. `source prepare_aws_enviroment.sh`
