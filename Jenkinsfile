pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        sh './mvnw package'
      }
    }

    stage('test') {
      steps {
        sh '''./mvnw test
'''
      }
    }

    stage('package') {
      steps {
        sh './mvnw package'
      }
    }

    stage('deploy') {
      steps {
        sh './mvnw deploy'
      }
    }

  }
}