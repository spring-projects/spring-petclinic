pipeline {
  agent any
  
  environment {
    GIT_COMMIT_SHORT = sh(script: "git rev-parse --short HEAD", returnStdout: true).trim()
    DOCKER_HUB_USER = "m1kky8"
    DOCKRHUB_CREDS = "580b959d-d40a-422f-a3d7-cf11b2ec7a4c"

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
        script {
          def branch = env.BRANCH_NAME
          sh """
            echo $BRANCH_NAME
          """
        }
      }
    }
  }
}
