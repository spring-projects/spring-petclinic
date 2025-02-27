pipeline {
  agent {
    docker { image 'maven:3.8.5-openjdk-17' }
  }

  stages {
    stage('Build') {
      steps {
        echo 'Bulding'
        checkout scm
        sh 'mvn checkstyle:checkstyle'
      }
    }
  }
}
