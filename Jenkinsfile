pipeline {
    agent any

    environment {
        DOCKER_REPO_MAIN = 'prathushadevijs/main'  // Replace with your Docker Hub or Nexus repository URL
        DOCKER_REPO_MR = 'prathushadevijs/mr'      // Replace with your Docker Hub or Nexus repository URL
        DOCKER_CREDENTIALS = 'b1305615-4b2e-42e3-97ad-c87166d45f54'  // Replace with Jenkins credentials ID for Docker Hub/Nexus
    }

    stages {
        stage('Checkout Code') {
            steps {
                script {
                    // Increase Git HTTP buffer size and configure to prevent the RPC failed error
                    sh 'git config --global http.postBuffer 524288000'  // Increase buffer size to 500MB
                    sh 'git config --global http.maxRequestBuffer 104857600'  // Increase request buffer size
                    sh 'git config --global core.askPass "echo"'  // Set Git to verbose logging

                    // Perform a shallow clone to reduce the repository size and avoid fetching the full history
                    retry(3) {
                        sh 'git fetch --depth=1'  // Shallow clone to get only the latest commit
                        checkout scm
                    }
                }
            }
        }

        stage('Checkstyle') {
            steps {
                script {
                    // Run Maven Checkstyle plugin using a Maven Docker image
                    docker.image('maven:3.8.4-openjdk-17-slim').inside {
                        sh 'mvn checkstyle:checkstyle'
                    }
                    archiveArtifacts allowEmptyArchive: true, artifacts: '**/checkstyle-result.xml'
                }
            }
        }

        stage('Test') {
            steps {
                script {
                    // Run tests with Maven using Maven Docker image
                    docker.image('maven:3.8.4-openjdk-17-slim').inside {
                        sh 'mvn test'
                    }
                }
            }
        }

        stage('Build') {
            steps {
                script {
                    // Build the application without running tests using Maven Docker image
                    docker.image('maven:3.8.4-openjdk-17-slim').inside {
                        sh 'mvn clean package -DskipTests'
                    }
                }
            }
        }

        stage('Create Docker Image for Merge Request') {
            when {
                branch 'mr/*'  // This will trigger the stage for a Merge Request branch
            }
            steps {
                script {
                    // Build Docker image for the merge request
                    def commitHash = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
                    // Authenticate Docker with Jenkins credentials and push to Docker Hub or Nexus
                    docker.withRegistry('https://index.docker.io/v1/', DOCKER_CREDENTIALS) {
                        sh "docker build -t ${DOCKER_REPO_MR}:${commitHash} ."
                        sh "docker push ${DOCKER_REPO_MR}:${commitHash}"
                    }
                }
            }
        }

        stage('Create Docker Image for Main Branch') {
            when {
                branch 'main'  // This will trigger the stage for the main branch
            }
            steps {
                script {
                    // Build Docker image for the main branch
                    def commitHash = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
                    // Authenticate Docker with Jenkins credentials and push to Docker Hub or Nexus
                    docker.withRegistry('https://index.docker.io/v1/', DOCKER_CREDENTIALS) {
                        sh "docker build -t ${DOCKER_REPO_MAIN}:${commitHash} ."
                        sh "docker push ${DOCKER_REPO_MAIN}:${commitHash}"
                    }
                }
            }
        }
    }

    post {
        always {
            cleanWs()  // Clean workspace after the pipeline run
        }
    }
}
