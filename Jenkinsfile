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
        sh 'mvn test'
      }
    }
 

    stage('Package') {
      steps {
        sh 'mvn package'
        slackSend channel: 'soen345', color: 'green', message: 'Success', tokenCredentialId: '178d0505-1fe0-4dee-aaa9-097c8a75a121'
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
  post{
    Success{
      slackSend channel: 'soen345', color: 'green', message: 'Success', tokenCredentialId: '178d0505-1fe0-4dee-aaa9-097c8a75a121'
    }
    
    failure{
      slackSend(color: '#FF0000' ,message: "FAILED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")
      
}
  }
}
