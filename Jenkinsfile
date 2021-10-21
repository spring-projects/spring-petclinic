pipeline {
    agent any
    stages {
        stage ('Build') {
            steps {
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
