pipeline {
  agent any
  stages {
    stage('checkStyle') {
      steps {
        sh './gradlew checkstyleMain'
      }
    }

  }
}