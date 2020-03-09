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
    }
    post {
        success {
        mail to: 'firassawan@icloud.com',
             subject: "Succeeded Pipeline: ${currentBuild.fullDisplayName}",
             body: "Email Notification: The build has successfully completed
            ${env.BUILD_URL}"
    }
        failure {
        mail to: 'firassawan@icloud.com',
             subject: "Failed Pipeline: ${currentBuild.fullDisplayName}",
             body: "Email Notification: The build has not completed successfully
            ${env.BUILD_URL}"
    }

    }
}
