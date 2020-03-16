pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                sh './mvnw build' 
            }
        }
        stage('Test') {
            steps {
                sh './mvnw test' 
            }
        }
        stage('Test') {
            steps {
                sh './mvnw test' 
            }
        }
        stage('Package') {
            steps {
                sh './mvnw package' 
            }
        }
        stage('Deploy') {
            steps {
                sh './mvnw deploy' 
            }
        }
    }
}
