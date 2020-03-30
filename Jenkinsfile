pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        bat './mvnw package'
        mail(subject: 'Build Test', body: 'Attempt 1', to: 'jessica.allaire.96@hotmail.com')
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
}