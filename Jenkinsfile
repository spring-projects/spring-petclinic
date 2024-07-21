pipeline {
    agent any

    environment {
        DOCKER_CREDENTIALS_ID = 'your-docker-credentials-id'  // Replace with your actual Docker credentials ID
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Build') {
            steps {
                script {
                    sh './mvnw clean package'
                }
            }
        }
        stage('Build Docker Image') {
            steps {
                script {
                    // Build the Docker image
                    sh 'docker build -t petclinic:latest .'
                }
            }
        }
        stage('Push Docker Image') {
            steps {
                script {
                    // Push the Docker image to a registry
                    docker.withRegistry('https://index.docker.io/v1/', "${DOCKER_CREDENTIALS_ID}") {
                        sh 'docker tag petclinic:latest your-docker-username/petclinic:latest'
                        sh 'docker push your-docker-username/petclinic:latest'
                    }
                }
            }
        }
        stage('Deploy') {
            steps {
                script {
                    // Deploy the application using Docker Compose
                    sh 'docker-compose up -d'
                }
            }
        }
    }
}
