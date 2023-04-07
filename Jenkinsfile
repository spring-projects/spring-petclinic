pipeline {
  agent any
  stages {
    stage('build') {
      steps {
        sh './mvnw clean package -Dmaven.test.skip=true '
      }
    }

    stage('test') {
      steps {
        echo 'test'
      }
    }

  }
}