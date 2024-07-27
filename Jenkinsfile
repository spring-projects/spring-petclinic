pipeline {
    agent any

    environment {
        SONARQUBE_URL = 'http://sonarqube:9000'
        SONARQUBE_CREDENTIALS_ID = 'admin'
        GITHUB_TOKEN = credentials('github-token')
    }

    // Define dockerImage at a higher scope to ensure availability in post block
    def dockerImage = null

    stages {
        stage('Checkout') {
            steps {
                script {
                    echo "Checking out code..."
                    git url: 'https://github.com/CChariot/spring-petclinic.git', branch: 'FinalProject_main', credentialsId: 'github-token'
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    echo "Building Docker Image..."
                    dockerImage = docker.build("spring-petclinic")
                    echo "Docker Image built: ${dockerImage.id}"
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                script {
                    echo "Starting SonarQube analysis..."
                    withSonarQubeEnv('SonarQube') {
                        dockerImage.inside("-u root") {
                            sh './mvnw sonar:sonar -Dsonar.projectKey=spring-petclinic'
                        }
                    }
                    echo "SonarQube analysis completed."
                }
            }
        }

        // Other stages omitted for brevity

    }

    post {
        always {
            script {
                try {
                    if (dockerImage != null) {
                        echo "Stopping and removing Docker Image..."
                        dockerImage.stop()
                        dockerImage.remove()
                    }
                } catch (Exception e) {
                    echo "Error during cleanup: ${e.message}"
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
