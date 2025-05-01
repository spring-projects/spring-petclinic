pipeline {
  agent {
    docker {
      image 'gradle:8.1.1-jdk17'
      args '-v /var/run/docker.sock:/var/run/docker.sock'
    }
  }

  environment {
    IMAGE_NAME = 'prankumar313-main'
  }

  stages {
    stage('Install Docker CLI') {
      steps {
        sh '''
          apt-get update
          apt-get install -y docker.io
        '''
      }
    }

    stage('Build Docker Image') {
      steps {
        script {
          COMMIT = sh(script: "git rev-parse --short HEAD", returnStdout: true).trim()
          sh "docker build -t ${IMAGE_NAME}:${COMMIT} ."
        }
      }
    }

    stage('Push to Docker Hub') {
      steps {
        withCredentials([usernamePassword(credentialsId: 'dockerhub-creds', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
          sh '''
            echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
            docker tag ${IMAGE_NAME}:${COMMIT} ${DOCKER_USER}/${IMAGE_NAME}:${COMMIT}
            docker push ${DOCKER_USER}/${IMAGE_NAME}:${COMMIT}
          '''
        }
      }
    }
  }
}
