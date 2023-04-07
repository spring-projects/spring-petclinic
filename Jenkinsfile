pipeline {
  agent any
  stages {
    stage('build') {
      steps {
        echo 'build'
        sh '''mvn package
'''
        sh 'ls'
      }
    }

    stage('run') {
      steps {
        echo 'run'
      }
    }

  }
}