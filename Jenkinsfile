pipeline {
  agent any
  stages {
    stage('build') {
      steps {
        sh './mvnw package  -Dmaven.test.skip'
      }
    }

    stage('test') {
      steps {
        echo 'test'
      }
    }

  }
}