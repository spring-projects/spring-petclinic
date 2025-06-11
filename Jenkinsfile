pipeline {
  agent any
  
  environment {
    GIT_COMMIT_SHORT = sh(script: "git rev-parse --short HEAD", returnStdout: true).trim()
    DOCKER_HUB_USER = "m1kky8"
    DOCKERHUB_CREDS = "580b959d-d40a-422f-a3d7-cf11b2ec7a4c"

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

    stage('Cred'){
      steps {
          withCredentials([usernamePassword(
              credentialsId: "580b959d-d40a-422f-a3d7-cf11b2ec7a4c",
              usernameVariable: 'DOCKER_USER',
              passwordVariable: 'DOCKER_PASS',
          )]) {
              echo "Logging in to Docker Hub..."
              sh "echo ${env.DOCKER_USER}"
          }

      }
    }

    stage('Build') {
      steps {
        script {
          def branch = env.BRANCH_NAME
          def dockerRepo = (branch == 'main') ? 'main' : 'mr'
          def imageTag = "${DOCKER_HUB_USER}/${dockerRepo}:${GIT_COMMIT_SHORT}"

          withCredentials([usernamePassword(
              credentialsId: DOCKERHUB_CREDS,
              usernameVariable: 'DOCKER_USER',
              passwordVariable: 'DOCKER_PASS'
          )]) {
              echo "Logging in to Docker Hub..."
              sh "echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin"
              echo "Pushing Docker image to ${imageTag}"
              sh "docker push ${imageTag}"
          }
        }
      }
    }
  }
}
