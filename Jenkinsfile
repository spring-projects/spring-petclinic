pipeline {
  agent none
  
  environment {
    GIT_COMMIT_SHORT = sh(script: "git rev-parse --short HEAD", returnStdout: true).trim()
    DOCKER_HUB_USER = "m1kky8"
    DOCKERHUB_CREDS = "580b959d-d40a-422f-a3d7-cf11b2ec7a4c"

  }

  stages {
    stage('checkStyle') {
      agent { label 'vps-agent1' }
      when { not {branch 'main' }}
      steps {
        echo "Syle Check"
        sh './gradlew checkstyleMain'
      }
    }

    stage('Test') {
      agent { label 'vps-agent1' }
      when { not {branch 'main' }}
      steps {
        echo "Running tests"
        sh './gradlew test'
      }
    }

    stage('Build') {
      agent { label 'vps-agent1' }
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
            sh 'echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin'
            
            echo "Building Docker image ${imageTag}..."
            sh "docker build -t ${imageTag} ."
            
            echo "Pushing Docker image ${imageTag}..."
            sh "docker push ${imageTag}"
          }
        }
      }
    }
  }
}
