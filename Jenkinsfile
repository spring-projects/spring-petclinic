pipeline {
    agent any

    tools {
        maven 'Maven3.9'
    }

    environment {
        MAVEN_HOME = tool 'Maven3.9'
    }

    stages {
        stage('Checkout') {
            steps {
                echo '🔄 Cloning the repository...'
                git branch: 'main', url: 'https://github.com/Hrushikesh43/spring-petclinic.git'
            }
        }

        stage('Build') {
            steps {
                echo '🔨 Compiling the project...'
                bat "${env.MAVEN_HOME}\\bin\\mvn clean compile"
            }
        }

        stage('Test') {
            steps {
                echo '🧪 Running unit tests...'
                bat "${env.MAVEN_HOME}\\bin\\mvn test"
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Package') {
            steps {
                echo '📦 Packaging application...'
                bat "${env.MAVEN_HOME}\\bin\\mvn package"
            }
        }

        stage('Deploy') {
            steps {
                echo '🚀 Deploying to target environment (placeholder)...'
                // Replace this with actual deployment script or SSH command
            }
        }

        stage('Archive') {
            steps {
                echo '📁 Archiving JAR...'
                archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
            }
        }
    }

    post {
        success {
            echo '✅ Pipeline finished successfully.'
        }
        failure {
            echo '❌ Pipeline failed. Check logs.'
        }
    }
}
