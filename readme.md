# Getting started

## Running a build pipeline

```
docker-compose -f docker-compose_spring-petclinic.yml  up -d
```

Go to the following locations:
* [petclinic](http://localhost:8080)
* [jenkins](http://localhost:8081)
* [sonarqube](http://localhost:9000)
* [promethius](http://localhost:8080/prometheus)
* [zap](http://localhost:8080/zap)


## Stopping a build pipeline
```
docker-compose -f docker-compose_spring-petclinic.yml  down
```

## Troubleshooting a container

See an event in a running container:
```
docker logs <container-id>
docker logs spring-petclinic-petclinic-1 
```

Run terminal in a running container:
```
docker exec -it <container-id> bash
docker exec -it  spring-petclinic-petclinic-1  bash
```

# Setting up Jenkins

## Steps 1: Set up Jenkins in Docker

1. Create a DockerFile with the following content:

    ```dockerfile
    FROM jenkins/jenkins:lts
    USER root
    RUN apt-get update && apt-get install -y docker.io
    USER jenkins
    ```

2. Build and Run Jenkins:

    ```bash
    docker build -t my-jenkins jenkins/
    docker run -d --name jenkins --network devsecops-network -p 8081:8081 -v jenkins_home:/var/jenkins_home my-jenkins
    ```

3. Go to [http://localhost:8081/](http://localhost:8081/)

4. Set up/install plugins

5. When asked for a password, run the following command:

    ```bash
    docker exec b946b28cf4b3ce018871fa319494d7add5e1d6806ee3a2e05bb6262c57a8b3a1 cat /var/jenkins_home/secrets/initialAdminPassword
    ```

    Access Jenkins at: [http://localhost:8081/jenkins](http://localhost:8081/jenkins)

## Steps 2: Create Jenkins Pipeline

1. Access Jenkins: Open [http://localhost:8081](http://localhost:8081) and set up Jenkins. Install the suggested plugins.

2. Install Required Plugins:
    - Go to Manage Jenkins > Manage Plugins and install the following plugins:
        - Pipeline
        - Git
        - GitHub Integration
        - Docker Pipeline

3. Create a New Pipeline Job:
    - Go to Jenkins Dashboard.
    - Click on **New Item**.
    - Enter a name for your pipeline (e.g., Spring PetClinic Pipeline).
    - Select **Pipeline** and click **OK**.

4. Configure the Pipeline:
    - In the pipeline configuration, scroll down to the Pipeline section.
    - Set **Definition** to **Pipeline script**.
    - Write the script to configure the Pipeline.

5. Run the Pipeline:
    - Save the Pipeline configuration by clicking **Save**.
    - Go back to the Jenkins dashboard.
    - Select your pipeline job.
    - Click on **Build Now** to run the pipeline.
