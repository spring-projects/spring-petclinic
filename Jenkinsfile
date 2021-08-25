pipeline {
  agent any
  stages {
    stage('show work directory') {
      steps {
        sh 'ls -la && pwd'
      }
    }

    stage('compile') {
      steps {
        sh 'mvn compile'
      }
    }

  }
}