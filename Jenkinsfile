pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                sh './mvnw clean compile'
            }
        }
        stage('Test') {
            steps {
                sh './mvnw test'
            }
        }
        stage('Package') {
            steps {
                echo './mvn package' 
            }
        }
        stage('Deploy') {
            steps {
                echo 'deploy' 
            }
        }
    }
}
