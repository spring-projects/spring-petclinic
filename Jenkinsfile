pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                sh './mvnw clean package'
            }
        }
        stage('Test') {
            steps {
                sh './mvnw test'
            }
        }
        stage('Package') {
            steps {
                echo 'package' 
            }
        }
        stage('Deploy') {
            steps {
                echo 'deploy' 
            }
        }
    }
}
