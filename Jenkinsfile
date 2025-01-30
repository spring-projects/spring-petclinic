pipeline {
    agent any
    environment {
        
        DOCKER_IMAGE = "prathushadevijs/spring-petclinic-proj"
        DOCKER_TAG = "${GIT_COMMIT}"
    }
    stages {
        stage('Checkout') {
            steps {
                checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[credentialsId: 'github-creds', url: 'https://github.com/Prathushadevijsgd/spring-petclinic']])
            }
        }
        stage('Build & Test') {
            steps {
                script {
                    // Build the Spring PetClinic app using Maven
                    sh 'mvn clean install -DskipTests'
                }
            }
        }
        stage('Build Docker Image') {
            steps {
                script {
                    // Build the Docker image from Dockerfile
                    sh 'docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} .'
                }
            }
        }
        stage('Push to Docker Hub') {
            steps {
                script {
                    // Login to Docker Hub
                    withCredentials([usernamePassword(credentialsId: 'dockerhub-creds', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                        sh 'docker login -u $DOCKER_USERNAME -p $DOCKER_PASSWORD'
                        // Push Docker image to Docker Hub
                        sh 'docker push ${DOCKER_IMAGE}:${DOCKER_TAG}'
                    }
                }
            }
        }
        stage('Deploy to EC2') {
            steps {
                script {
                    // Deploy the Docker image to EC2 (or ECS)
                    withCredentials([sshUserPrivateKey(credentialsId: 'ec2-ssh-key', keyFileVariable: 'KEYFILE')]) {
                        sh """
                            ssh -i $KEYFILE ec2-user@44.211.188.214 'docker pull ${DOCKER_IMAGE}:${DOCKER_TAG} && \
                            docker stop spring-petclinic || true && \
                            docker rm spring-petclinic || true && \
                            docker run -d --name spring-petclinic -p 8080:8080 ${DOCKER_IMAGE}:${DOCKER_TAG}'
                        """
                    }
                }
            }
        }
    }
}
