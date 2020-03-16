pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
               bat  './mvnw compile'
            }
        }
       
    }
    post {
    success {
        emailext body: 'A Test EMail', 
                recipientProviders: [[$class: 'DevelopersRecipientProvider'], 
                                     [$class: 'RequesterRecipientProvider']], 
                subject: 'Test'
    }
}
}
