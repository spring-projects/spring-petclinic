pipeline {
  agent any
  stages {
    stage('build') {
      steps {
        echo 'build'
        sh '''mvn package
'''
      }
    }

    stage('run') {
      steps {
        echo 'run'
      }
    }

  }
}