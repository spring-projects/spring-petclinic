pipeline {
  agent any
  stages {
    stage('build') {
      steps {
        sh 'mvn package'
      }
    }

    stage('test') {
      steps {
        echo 'test'
      }
    }

  }
}