pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "myusername/spring-petclinic"
        DOCKER_REGISTRY = "docker.io"
    }

    stages {
        // This stage runs only for a Merge Request (Pull Request)
        stage('Checkstyle') {
            when {
                changeRequest()
            }
            steps {
                script {
                    // Run Maven checkstyle plugin to generate a code style report
                    sh './mvnw checkstyle:checkstyle'
                }
                // Archive the checkstyle report as a job artifact
                archiveArtifacts artifacts: 'target/checkstyle-result.xml', allowEmptyArchive: true
            }
        }

        stage('Test') {
            when {
                changeRequest()
            }
            steps {
                script {
                    // Run tests with Maven or Gradle
                    sh './mvnw test'
                }
            }
        }

        stage('Build') {
            when {
                changeRequest()
            }
            steps {
                script {
                    // Build without running tests (faster build)
                    sh './mvnw clean install -DskipTests'
                }
            }
        }

        stage('Create Docker Image - MR') {
            when {
                changeRequest()
            }
            steps {
                script {
                    // Build the Docker image and tag it with GIT_COMMIT (short version)
                    def gitCommit = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
                    docker.build("${DOCKER_IMAGE}:mr-${gitCommit}")
                    
                    // Push the image to the "mr" repository
                    docker.withRegistry("https://${DOCKER_REGISTRY}", 'docker-hub-credentials') {
                        docker.image("${DOCKER_IMAGE}:mr-${gitCommit}").push('mr')
                    }
                }
            }
        }

        // This stage runs only for the main branch
        stage('Create Docker Image - Main') {
            when {
                branch 'main'
            }
            steps {
                script {
                    // Build the Docker image and push it to the "main" repository
                    docker.build("${DOCKER_IMAGE}:latest")
                    
                    // Push the image to the "main" repository
                    docker.withRegistry("https://${DOCKER_REGISTRY}", 'docker-hub-credentials') {
                        docker.image("${DOCKER_IMAGE}:latest").push('main')
                    }
                }
            }
        }
    }

    post {
        always {
            // Clean up any resources, or notify after the build
            cleanWs()
        }
    }
}
