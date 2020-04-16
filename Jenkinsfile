pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                sh './mvnw package' 
            }
        }
        stage('Test') {
            steps {
                echo 'Test'
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
