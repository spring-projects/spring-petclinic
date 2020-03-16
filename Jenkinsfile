pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                sh './mvnw compile' 
            }
        }
	stage('Test') {
            steps {
                sh './mvnw test' 
            }
        }
	stage('Package') {
            steps {
                sh './mvnw package' 
            }
        }
	stage('Deploy') {
            when {
	        branch 'master'
            }
            steps {
                sh './mvnw deploy' 
            }
        }
    }
    post {
        success {
           slackSend channel: 'jenkins', color: '00FF00', message: 'Success', teamDomain: 'uni-vaz6979', tokenCredentialId: 'dp2tuBLCh5PBUszexU45zIxt'
        }
        failure {
           slackSend channel: 'jenkins', color: '00FF00', message: 'Failed', teamDomain: 'uni-vaz6979', tokenCredentialId: 'dp2tuBLCh5PBUszexU45zIxt'
        }
    }
}

