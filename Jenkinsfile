pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
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
    } post {
        success {
            mail to:"firassawan@icloud.com", subject:"SUCCESS: ${currentBuild.fullDisplayName}", body: "Yay, we passed."
        }
        failure {
            mail to:"firassawan@icloud.com", subject:"FAILURE: ${currentBuild.fullDisplayName}", body: "Boo, we failed."
        }
    }
}
