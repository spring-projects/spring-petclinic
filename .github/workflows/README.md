# Spring Pet Clinic Pipeline

This repository contains a GitHub Actions pipeline to automate the build, test, and publish processes for the Spring Pet Clinic project. The pipeline includes steps for compiling the code, running tests, packaging the project as a Docker image, scanning the code with a Software Composition Analysis (SCA) tool and publishing the Docker image to JFrog Artifactory.

## Pipeline Steps

### 1. Compile Code
This step compiles the source code using Maven.

### 2. Run Tests
Execute Maven tests to ensure the codebase integrity.

### 3. Package as Docker Image
Build a Docker image containing the Spring Pet Clinic application using a dedicated Dockerfile.

### 4. Scan with SCA Tool
Perform a security scan on the codebase using the Software Composition Analysis (SCA) tool by JFrog.

### 5. Publish to JFrog Artifactory
The pipeline publishes the Docker image to JFrog Artifactory using JFrog CLI commands.

## How to Run the Project

1. Ensure you have Docker installed on your machine.

2. Run the following command to build and run the Docker image:

    ```bash
    docker run -p 8080:8080 danvid.jfrog.io/spring-petclinic-docker-local/spring-petclinic
    ```

3. Access the Spring Pet Clinic application at [http://localhost:8080/](http://localhost:8080/).

## Additional Information

- **Dependency Resolution:** All project dependencies are resolved from Maven Central. The `pom.xml` file in the project has been updated accordingly.

- **JFrog Artifactory:** The Docker image is published to JFrog Artifactory. The build metadata is also included using "jf rt build-publish."

- **Scan Data Report:** The Software Composition Analysis (SCA) tool generates a detailed report, which is available as an artifact named "sca_report" in every GitHub Actions workflow run.