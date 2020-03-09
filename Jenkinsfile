pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                sh 'mvn clean' 
            }
        }
        stage('Test') {
            steps {
                sh 'mvn test' 
            }
        }
        stage('Package') {
            steps {
                sh 'mvn package' 
            }
        } 
        stage('Deploy') {
            steps {
                sh 'mvn deploy' 
            }
        }
    }
    post {
        success {
        mail to: 'firassawan@icloud.com',
             subject: "Succeeded Pipeline: ${currentBuild.fullDisplayName}",
             body: "Email Notification: The build has successfully completed ${env.BUILD_URL}"
    }
       failure {
        mail to: 'firassawan@icloud.com',
             subject: "Failed Pipeline: ${currentBuild.fullDisplayName}",
             body: "Email Notification: The build has not completed successfully ${env.BUILD_URL}"
    }

    }
}
