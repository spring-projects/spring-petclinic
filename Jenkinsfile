pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                bat './mvnw package' 
            }
        }
        stage('Test') {
            steps {
                bat './mvnw test'
                }
        }
        stage('Deploy') {
            when {
                branch 'master'
            }
            steps {
                bat './mvnw deploy'
            }
        }
    }
}
