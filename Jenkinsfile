pipeline {
  agent any
  stages {
    stage('build') {
      steps {
        echo 'build'
        sh './mvnw spring-boot:build-image'
      }
    }

    stage('run') {
      steps {
        echo 'run'
      }
    }

  }
}