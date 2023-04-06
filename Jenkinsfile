pipeline {
  agent any
  stages {
    stage('build') {
      steps {
        echo 'build'
        sh '''mvn clean install
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