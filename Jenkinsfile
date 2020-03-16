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
    success {
        emailext 'mnezam.31@gmail.com',
        body: 'A Test EMail', 
                recipientProviders: [[$class: 'DevelopersRecipientProvider'], 
                                     [$class: 'RequesterRecipientProvider']], 
                subject: 'Test'
    }
}
}
