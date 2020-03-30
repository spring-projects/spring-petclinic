pipeline {
  agent any
   stages {
      stage('Build') {
        steps {
          bat './mvnw package'
        }
      }
     stage('Test') {
      steps {
        echo 'Testing'
      }
    }

    stage('Package') {
      steps {
        echo "blob"
      }
    }

    stage('Deploy') {
      when {
        expression {
          GIT_BRANCH == 'master'
        }
      }
      steps {
        echo 'Deploy'
      }
    }
   }
post {
  always {
      slackSend "The pipeline ${currentBuild.fullDisplayName} completed successfully."
  }
}
