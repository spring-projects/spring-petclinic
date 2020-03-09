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
                sh './gradlew check'
            }
        }
    }
}
