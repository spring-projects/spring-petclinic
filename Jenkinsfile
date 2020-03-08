pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                sh 'mvn spring-javaformat:apply'
                sh './mvnw package' 
            }
        }
    }
}
