# 

----

# JFrog Spring PetClinic

This repository is a customized fork of the original [Spring PetClinic](https://github.com/spring-projects/spring-petclinic).

## Modifications

The following enhancements and changes have been made to the original project:

- **Upgrade to JVM 21**: The project now runs on the latest JVM version 21 for improved performance and compatibility.
- **Security Improvements**: Resolved dependency issues related to:
   - [CVE-2021-26291](https://nvd.nist.gov/vuln/detail/CVE-2021-26291)
   - [CVE-2024-25710](https://nvd.nist.gov/vuln/detail/CVE-2024-25710)

## Continuous Integration (CI) Pipeline

A robust CI pipeline has been set up with the following automated steps:

1. **Build**: Compile the project using Maven.
2. **Test**: Run tests using Maven to ensure code quality and reliability.
3. **Dockerize**: Build and tag the Docker container for deployment.
4. **Security Scan**: Launch a vulnerability scan to maintain robust security standards.

## Docker Instructions

The `Dockerfile` is located in the root directory. You can run the application locally on a Linux environment using Docker with the following command:

```other
docker run -d -p 8080:8080 rodi26.jfrog.io/rodi26-docker-local/jfrog-spring-petclinic:latest
```

This command will start the application in detached mode, making it accessible on port 8080.
