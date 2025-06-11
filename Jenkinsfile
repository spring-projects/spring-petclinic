pipeline {
  agent any
  
  environment {
    GIT_COMMIT_SHORT = sh(script: "git rev-parse --short HEAD", returnStdout: true).trim()
    DOCKER_HUB_USER = "m1kkY8"
  }

  stages {
    stage('checkStyle') {
      when { not {branch 'main' }}
      steps {
        sh './gradlew checkstyleMain'
      }
    }

    stage('Test') {
      when { not {branch 'main' }}
      steps {
        sh './gradlew test'
      }
    }

    stage('Build') {
      steps {
        sh './gradlew build -x test'
      }
    }
  }
}
