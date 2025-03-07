pipeline {
    agent any
    
    environment {
        // Define environment variables
        DOCKER_REGISTRY = "docker.io"
        DOCKER_IMAGE_MAIN = 'mmarcetic/main'
        DOCKER_IMAGE_MR = 'mmarcetic/mr'
        GIT_COMMIT = sh(script: "git rev-parse --short HEAD", returnStdout: true).trim()
    }
    
    stages {
        stage('Checkout') {
            steps {
                // Checkout the code from the repository
                checkout scm
            }
        }

        stage('Set Docker Image') {
            steps {
                script {
                    // Set Docker image based on the branch name
                    if (env.BRANCH_NAME == 'main') {
                        env.DOCKER_IMAGE = DOCKER_IMAGE_MAIN
                    } else {
                        env.DOCKER_IMAGE = DOCKER_IMAGE_MR
                    }

                    echo "Using Docker image: ${env.DOCKER_IMAGE}"
                }
            }
        }

        stage('Checkstyle Report') {
            when {
                    changeRequest()
                }
            steps {
                script {
                    // Checkstyle with Gradle
                    sh './gradlew checkstyleMain'
                    archiveArtifacts artifacts: 'build/reports/checkstyle/*.xml', allowEmptyArchive: true
                }
            }
        }

        stage('Test') {
            when {
                changeRequest()
            }
            steps {
                // Test using Gradle
                sh './gradlew clean test'
            }
        }

        stage('Build Without Tests') {
            when {
                changeRequest()
            }
            steps {
                script {
                    // Build without tests using Gradle
                    sh './gradlew build -x test'
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    // Build the Docker image
                    sh "docker build -t ${DOCKER_REGISTRY}/${DOCKER_IMAGE}:${GIT_COMMIT} ."
                }
            }
        }

        stage('Push Docker Image for Change Request') {
            when {
                    changeRequest()
            }
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: "docker-login", usernameVariable: "DOCKER_USER", passwordVariable: "DOCKER_PASSWORD")]) {
                        sh "docker login -u ${DOCKER_USER} -p ${DOCKER_PASSWORD}"
                        sh "docker push ${DOCKER_REGISTRY}/${DOCKER_IMAGE}:${GIT_COMMIT}"
                    }
                }
            }
        }

        stage('Push Docker Image for Main') {
            when {
                    branch "main"
            }
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: "docker-login", usernameVariable: "DOCKER_USER", passwordVariable: "DOCKER_PASSWORD")]) {
                        sh "docker login -u ${DOCKER_USER} -p ${DOCKER_PASSWORD}"
                        sh "docker push ${DOCKER_REGISTRY}/${DOCKER_IMAGE}:${GIT_COMMIT}"
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

