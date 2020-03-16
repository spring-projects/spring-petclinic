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
                echo 'Deploy'
            }
        }
    }
    post {
        success {
        mail to: 'mnezam.31@gmail.com',
                subject: "Successful Pipeline",
                body: "Your tests worked accordingly!"
        }   
        failure {
            mail to: 'mnezam.31@gmail.com',
                subject: "Failed Pipeline}",
                body: "Something is wrong"
    }
}
}
