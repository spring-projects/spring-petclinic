pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                mvn spring-javaformat:apply
                sh './mvnw package' 
            }
        }
    }
}
