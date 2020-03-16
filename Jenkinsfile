pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
               bat  './mvnw compile'
            }
        }
         stage('Test') {
            steps {
                bat './mvnw test'
            }
        }
        stage('Package') {
            steps {
                bat './mvnw package'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deployed Successfully'
            }
        }  
    }
    post {
    always {
        emailext body: 'A Test EMail', 
                recipientProviders: [[$class: 'DevelopersRecipientProvider'], 
                                     [$class: 'RequesterRecipientProvider']], 
                subject: 'Test'
    }
}
}
