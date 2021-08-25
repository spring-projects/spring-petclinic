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

    stage('test') {
      steps {
        sh 'mvn test'
      }
    }

    stage('build app') {
      steps {
        sh 'mvn package -Dmaven.test.skip=true'
      }
    }

  }
}