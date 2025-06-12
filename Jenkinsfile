pipeline {
    agent any

    environment {
        GIT_COMMIT_SHORT = sh(script: "git rev-parse --short HEAD", returnStdout: true).trim()
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        // === Merge Request (MR) jobs only ===
        stage('Checkstyle') {
            when {
                expression { return env.CHANGE_ID != null }
            }
            steps {
                sh 'mvn checkstyle:checkstyle'
            }
            post {
                always {
                    archiveArtifacts artifacts: 'target/checkstyle-result.xml', fingerprint: true
                }
            }
        }

        stage('Test') {
            when {
                expression { return env.CHANGE_ID != null }
            }
            steps {
                sh 'mvn test'
            }
        }

        stage('Build (no tests)') {
            when {
                expression { return env.CHANGE_ID != null }
            }
            steps {
                sh 'mvn package -DskipTests'
            }
        }

        // === Docker build and push for both MR and main ===
        stage('Build & Push Docker Image') {
            steps {
                script {
                    def isMR = env.CHANGE_ID != null

                    // Define repo and tag based on context
                    def repo = isMR ? "ggonzalezx/mr" : "ggonzalezx/main"

                    def tag = isMR ? "${GIT_COMMIT_SHORT}" : "latest"

                    def fullImageName = "${repo}:${tag}"

                    echo "Building Docker image with Dockerfile.two: ${fullImageName}"

                    // Build using Dockerfile.two
                    docker.build(fullImageName, "-f Dockerfile.two .")

                    // Authenticate and push to Docker Hub
                    docker.withRegistry('https://index.docker.io/v1/', 'dockerhub-credentials') {
                        docker.image(fullImageName).push()
                    }

                    echo "Docker image pushed to: https://hub.docker.com/repository/docker/${repo}"
                }
            }
        }
    }

    post {
        success {
            echo "Pipeline SUCCESS for ${env.BRANCH_NAME} (${env.CHANGE_ID ?: 'not an MR'})"
        }
        failure {
            echo "Pipeline FAILURE for ${env.BRANCH_NAME} (${env.CHANGE_ID ?: 'not an MR'})"
        }
    }
}
