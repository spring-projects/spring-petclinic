pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                bat './mvnw build' 
            }
        }
        stage('Test') {
            steps {
                bat './mvnw test' 
            }
        }
        stage('Package') {
            steps {
                bat './mvnw package' 
            }
        }
        stage('Deploy') {
            steps {
                bat './mvnw deploy' 
            }
        }
    }
}
