pipeline {
    agent any
    
    stages {
        stage('Build') {
            when { branch 'master' }
            steps {
                sh './mvnw compile' 
            }
        }
        
        stage('Test') {
            when { branch 'master' }
            steps {
                sh './mvnw test' 
            }
        }
        
        stage('Package') {
            when { branch 'master' }
            steps {
                sh './mvnw package' 
            }
        }
    }
    
    post {
    success {
      slackSend (color: '#00FF00', message: "SUCCESSFUL: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")
    }

    failure {
      slackSend (color: '#FF0000', message: "FAILED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")
    }
  }
}
