pipeline {
  agent any
  stages {
    stage('Build using Maven') {
      steps {
        sh 'mvn package'
      }
    }

    stage('build docker image') {
      steps {
        sh 'docker build -t petclinicapp:$BUILD_ID .'
      }
    }

  }
}