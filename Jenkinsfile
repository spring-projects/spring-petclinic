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
    success{
      slackSend channel: 'soen345', message: 'Success', tokenCredentialId: '178d0505-1fe0-4dee-aaa9-097c8a75a121'
    }
    
    failure{
      slackSend channel: 'soen345', message: 'Failed', tokenCredentialId: '178d0505-1fe0-4dee-aaa9-097c8a75a121'


  }
      
}
  }
}
