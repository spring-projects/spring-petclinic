pipeline {
    agent any
    stages{
        stage('Build') {
            steps {
                sh './mvnw install'
            }
        }
        stage('Test') {
            steps {
                sh './mvnw test'
            }
        }
        stage('Deploy') {
            steps {
                sh './mvnw deploy'
            }
        }

    }
}