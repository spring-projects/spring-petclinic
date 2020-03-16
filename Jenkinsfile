pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                echo 'build'
            }
        }

    }
    post{
        success{
            slackSend (message: "${buildStatus} ${env.JOB_NAME} [${env.BUILD_NUMBER}] (${env.BUILD_URL})")
        }
    }
}
