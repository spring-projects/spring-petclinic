pipeline {
    agent any
	tools {
        maven 'maven 3.6.3'
        jdk 'jdk-13'
    }
    stages {
        stage('Build') {
            steps {
                sh 'mvn clean compile' 
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
            when {
                branch 'master'
            }
            steps {
                sh 'mvn deploy' 
            }
        }
    }
	post {
		failure {
			sendSlackNotification(currentBuild.result)
		}
		success{
			sendSlackNotification(currentBuild.result)
		}
	}
}

def sendSlackNotification(String buildStatus = 'FAILURE'){
      buildStatus =  buildStatus ?: 'FAILURE'
      def colorName = 'RED'
      def colorCode = '#FF0000'
      def subject = "${buildStatus}: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'"
      def summary = "${subject}"
      def time_info = ""
	if (buildStatus == 'SUCCESS') {
        color = 'GREEN'
        colorCode = '#388e3c'
        time_info = '\nPipeline completed at '+ new Date(currentBuild.startTimeInMillis+currentBuild.duration).format("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        summary = "${summary} ${time_info}"
      }
      slackSend color: colorCode, message: summary
}
