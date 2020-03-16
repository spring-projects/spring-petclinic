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
                bat './mvnw deploy:deploy-file'
            }
        }
    }
    post {
        success {
        mail to: 'mnezam.31@gmail.com',
                subject: "Successful Pipeline: ${currentBuild.fullDisplayName}",
                body: "Your tests worked accordingly!"
        }   
        failure {
            mail to: 'mnezam.31@gmail.com',
                subject: "Failed Pipeline: ${currentBuild.fullDisplayName}",
                body: "Something is wrong with ${env.BUILD_URL}"
    }
}
}
