pipeline {
    agent any
    
    environment {
        // Define environment variables
        DOCKER_REGISTRY = "docker.io"
        DOCKER_IMAGE = "mmarcetic/main"
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
                    def gitCommit = sh(script: "git rev-parse --short HEAD", returnStdout: true).trim()
                    sh "docker build -t ${DOCKER_REGISTRY}/${DOCKER_IMAGE}:${gitCommit} ."
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    def gitCommit = sh(script: "git rev-parse --short HEAD", returnStdout: true).trim()
                    withCredentials([usernamePassword(credentialsId: "docker-login", usernameVariable: "DOCKER_USER", passwordVariable: "DOCKER_PASSWORD")]) {
                        sh "docker login -u ${DOCKER_USER} -p ${DOCKER_PASSWORD}"
                        sh "docker push ${DOCKER_REGISTRY}/${DOCKER_IMAGE}:${gitCommit}"
                    }
                }
            }
        }
    }

    post {
        success {
            echo 'Docker image built and pushed successfully.'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}

