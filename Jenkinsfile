pipeline {
    agent any
    stages {
        slackSend "Build Started - ${env.JOB_NAME} ${env.BUILD_NUMBER} (<${env.BUILD_URL}|Open>)" 
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
}
