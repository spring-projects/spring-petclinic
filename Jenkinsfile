pipeline {
    agent any
    tools {
        maven 'Maven 3.3.9'
        jdk 'jdk8'
    }
    stages {
        stage ('Build') {
            steps {
                sh 'cd spring-petclinic'
                sh './mvnw package'
            }
            post {
                success {
                    junit 'target/*.jar' 
                }
            }
        }
    }
}
