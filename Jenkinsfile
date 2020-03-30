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
        echo 'Package'
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
    success {
        slackSend color: 'good',
                  message: "The pipeline ${currentBuild.fullDisplayName} completed successfully."
    }
}
