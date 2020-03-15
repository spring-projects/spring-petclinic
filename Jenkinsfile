pipeline {

    agent any
    stages {
        stage('Build') {
            steps {
                slackSend channel: 'notif', message: "Successful build!"
                sh './mvnw package' 
            }
        }
        stage('Test') {
            steps {
                sh './mvnw package' 
            }
        }
        stage('Package') {
            steps {
                sh './mvnw package' 
            }
        }
        stage('Deploy') {
            steps {
                sh './mvnw package' 
            }
        }
    }
}
