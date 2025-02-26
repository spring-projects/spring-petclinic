pipeline {
  agent any

  tools {
    maven 'M3'
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
