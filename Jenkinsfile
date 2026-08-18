pipeline {
    agent any

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh './mvnw clean package -DskipTests'
            }
        }

        stage('Test') {
            steps {
                sh './mvnw test'
            }
        }

    }

    post {
        success {
            echo 'Build and tests completed successfully!'
        }

        failure {
            echo 'Build or tests failed.'
        }
    }
}
