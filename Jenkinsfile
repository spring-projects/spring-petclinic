pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                sh './mvnw package'
            }
        }

        stage('Testing') {
            steps {
                echo 'Testing'
            }
        }

        stage('Package') {
            steps {
                echo 'Packaging'
            }
        }

        stage('Deploy') {
            steps {
                echo 'Deploying'
            }
        }
    }
    slackSend (message: "${buildStatus} ${env.JOB_NAME} ${env.BUILD_NUMBER} (${env.BUILD_URL})")
}
