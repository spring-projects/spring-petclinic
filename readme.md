
# Getting started - Final Project

## Running a build pipeline

```bash
docker-compose -f docker-compose_spring-petclinic.yml up -d
```

Go to the following locations:
* [petclinic](http://localhost:8080)
* [jenkins](http://localhost:8081)
* [sonarqube](http://localhost:9000)
* [prometheus](http://localhost:9090)
* [grafana](http://localhost:3000)
* [zap](http://localhost:8080/zap)

## Stopping a build pipeline

```bash
docker-compose -f docker-compose_spring-petclinic.yml down
```

## Troubleshooting a container

See an event in a running container:

```bash
docker logs <container-id>
docker logs spring-petclinic-petclinic-1 
```

Run terminal in a running container:

```bash
docker exec -it <container-id> bash
docker exec -it spring-petclinic-petclinic-1 bash
```

# Setting up Jenkins

## Steps 1: Set up Jenkins in Docker

1. Create a `Dockerfile.jenkins` with the following content:

    ```dockerfile
    FROM jenkins/jenkins:lts
    USER root
    RUN apt-get update && apt-get install -y docker.io
    RUN jenkins-plugin-cli --plugins prometheus configuration-as-code
    COPY jenkins.yaml /var/jenkins_home/casc_configs/jenkins.yaml
    ENV CASC_JENKINS_CONFIG=/var/jenkins_home/casc_configs/jenkins.yaml
    USER jenkins
    EXPOSE 8080
    ```

2. Build and Run Jenkins:

    ```bash
    docker build -t my-jenkins -f Dockerfile.jenkins .
    docker run -d --name jenkins --network devsecops-network -p 8081:8080 -v jenkins_home:/var/jenkins_home my-jenkins
    ```

3. Go to [http://localhost:8081/](http://localhost:8081/)

4. Set up/install plugins.

5. When asked for a password, run the following command:

    ```bash
    docker exec -it jenkins cat /var/jenkins_home/secrets/initialAdminPassword
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
    - Use the following script to configure the Pipeline:

    ```groovy
    pipeline {
        agent any

        environment {
            SONARQUBE_URL = 'http://sonarqube:9000'
            SONARQUBE_CREDENTIALS_ID = 'admin'
            GITHUB_TOKEN = credentials('github-token')
        }

        stages {
            stage('Checkout') {
                steps {
                    script {
                        echo "Checking out code..."
                        git url: 'https://github.com/CChariot/spring-petclinic.git', branch: 'FinalProject_main', credentialsId: 'github-token'
                    }
                }
            }

            stage('Build JAR') {
                steps {
                    script {
                        echo "Building JAR..."
                        sh './mvnw clean package -Dmaven.test.skip=true'
                    }
                }
            }

            stage('Build Docker Image') {
                steps {
                    script {
                        echo "Building Docker Image..."
                        def dockerImage = docker.build("spring-petclinic", "--no-cache .")
                        echo "Docker Image built: ${dockerImage.id}"
                        env.DOCKER_IMAGE_ID = dockerImage.id
                    }
                }
            }

            // Further stages would reference env.DOCKER_IMAGE_ID if needed
        }

        post {
            always {
                script {
                    if (env.DOCKER_IMAGE_ID) {
                        echo "Stopping and removing Docker Image with ID: ${env.DOCKER_IMAGE_ID}"
                        sh "docker rmi -f ${env.DOCKER_IMAGE_ID}"
                    }
                }
            }
            success {
                echo 'Pipeline completed successfully!'
            }
            failure {
                echo 'Pipeline failed.'
            }
        }
    }
    ```

5. Run the Pipeline:
    - Save the Pipeline configuration by clicking **Save**.
    - Go back to the Jenkins dashboard.
    - Select your pipeline job.
    - Click on **Build Now** to run the pipeline.

## Steps 3: Running Static Analysis with Sonarqube

1. Access Sonarqube: Open [http://localhost:9000](http://localhost:9000) and set up Sonarqube. Install the suggested plugins.

2. Login to Sonarqube with the following user and password:
    - Username: admin
    - Password: admin (update password when prompted after login)

3. Create project on Sonarqube:
    - Navigate over to create project
    - set the following variables:
        - Project display name = petclinic
        - Project key = petclinic
        - Main branch name = main
    - Choose the following option: global branch setting
    - Choose the following Analysis Method: Locally
    - Generate a project token

4. Set sonar token:
    - Copy the generated project token to clipboard
    - In your workspace, set the token environment variable (MY_SONAR_TOKEN)

    ```bash
    export MY_SONAR_TOKEN=<paste_token_here>
    ```

5. Run static analysis:

    ```bash
    docker run         --rm         -e SONAR_HOST_URL=http://sonarqube:9000/         -e SONAR_TOKEN=$MY_SONAR_TOKEN         -v "$(pwd):/usr/src" --network=spring-petclinic_custom-network         sonarsource/sonar-scanner-cli
    ```

## Steps 4: Setting Up Prometheus and Grafana

1. Access Prometheus: Open [http://localhost:9090](http://localhost:9090) and set up Prometheus.

2. Access Grafana: Open [http://localhost:3000](http://localhost:3000) and set up Grafana.
    - Username: admin
    - Password: admin (update password when prompted after login)

3. Configure Grafana to use Prometheus as a data source:
    - Go to Grafana Dashboard > Configuration > Data Sources > Add data source.
    - Select Prometheus.
    - Set the URL to `http://prometheus:9090`.
    - Click Save & Test to verify the configuration.

## Steps 5: Running OWASP ZAP

1. Access ZAP: Open [http://localhost:8080/zap](http://localhost:8080/zap) and set up ZAP.

2. Run OWASP ZAP with the appropriate configuration:

    ```bash
    docker run -v $(pwd)/zap-report:/zap/wrk:rw -t owasp/zap2docker-stable zap-baseline.py -t http://petclinic:8080 -g gen.conf -r zap-report.html
    ```

3. Add post-build actions to publish HTML reports for OWASP ZAP:
    - In Jenkins, go to your pipeline job > Configure.
    - In the Post-build Actions section, add a publish HTML report step.
    - Set the HTML directory to `zap-report` and the report files to `zap-report.html`.

## Steps 6: Deploying to Production

1. Set up a Virtual Machine (VM) to act as the production web server.

2. Use Ansible on the Jenkins build server to deploy the spring-petclinic application to the production web server (VM).
    - Create an Ansible playbook to deploy the application.
    - Add an Ansible playbook step in Jenkins to run the playbook after the build is successful.

## Deliverables

### Step-by-Step Instructions

Provide detailed documentation outlining the steps to set up the environment and configure the DevSecOps pipeline.

### Provisioning Scripts and Configuration Files

Submit the Docker commands and scripts used to set up Jenkins, SonarQube, Prometheus, Grafana, OWASP ZAP, and Ansible playbook (e.g., Dockerfiles, Vagrant files, Groovy scripts, YAMLs, INIs, etc.).

### Screenshots

1. Include a screenshot of the spring-petclinic welcome screen on the production web server.
2. Include screenshots of key Jenkins, Sonarqube, Prometheus, Grafana, and OWASP ZAP screens.
3. Provide evidence showing that a code change triggers the pipeline, deployment is done, and the content of the application is automatically updated. Show the deployed version is different from before the code was committed.

### Pipeline Demonstration

1. Provide a short video demonstrating the automated build, deployment, and monitoring process in Jenkins, Sonarqube, Prometheus, and Grafana. (Link for the video in Project Documents).

### Advanced Automated Scripting

Fully automated assignment completion will receive a bonus
