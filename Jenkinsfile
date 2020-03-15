pipeline {

environment {
     
        SLACK_CHANNEL = "#gayfuckassignment"
        SLACK_TEAM_DOMAIN = "yeetus345"
        SLACK_TOKEN = credentials("ilhAultZiny2Kbijeb1h6XCC")
        DEPLOY_URL = "http://localhost:9090/job/spring-petclinic"
    }

    agent any
    stages {
        stage('Build') {
            steps {
                slackSend channel: 'notif', message: "Successful build!"
                sh './mvnw package' 
            }
            post {
                success {
                    slackSend(
                            teamDomain: "${env.SLACK_TEAM_DOMAIN}",
                            token: "${env.SLACK_TOKEN}",
                            channel: "${env.SLACK_CHANNEL}",
                            color: "good",
                            message: "I absolutetly hate this, but hey the build succeeded"
                    )
                }
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
