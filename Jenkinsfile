pipeline {
    agent any
    
    environment {
        // Define environment variables
        DOCKER_REGISTRY = "docker.io"
        DOCKER_IMAGE = "mmarcetic/main"
        DOCKER_CREDENTIALS = "Docker_hub" 
    }
    
    stages {
        stage('Checkout') {
            steps {
                // Checkout the code from the repository
                checkout scm
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    // Build the Docker image
                    sh 'docker build -t ${DOCKER_REGISTRY}/${DOCKER_IMAGE}:latest .'
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                //Login to the Docker repository 
                    docker.withRegistry('https://${DOCKER_REGISTRY}', "${DOCKER_CREDENTIALS}") {
                        // Push the Docker image to the registry
                        sh 'docker push ${DOCKER_REGISTRY}/${DOCKER_IMAGE}:latest'
                    }
                }
            }
        }
    }

    post {
        always {
            // Clean up Docker images after the job is done
            sh 'docker rmi ${DOCKER_REGISTRY}/${DOCKER_IMAGE}:latest || true'
        }
        success {
            echo 'Docker image built and pushed successfully.'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}

