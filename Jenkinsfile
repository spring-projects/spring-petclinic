pipeline {
    agent any
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
