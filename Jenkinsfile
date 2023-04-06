pipeline {
  agent any
  stages {
    stage('build') {
      steps {
        echo 'build'
        sh '''cd spring-petclinic
./mvnw spring-boot:build-image'''
      }
    }

    stage('run') {
      steps {
        echo 'run'
      }
    }

  }
}