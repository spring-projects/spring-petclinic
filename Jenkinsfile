final boolean doNotSkip = false;

pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                sh './mvnw clean' 
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
            when (doNotSkip) {
                steps {
                sh './mvnw deploy' 
                }
            }
        }
    }
}
