pipeline {
  agent {
    docker {
      image 'gradle:8.1.1-jdk17'
      args '-v /var/run/docker.sock:/var/run/docker.sock'
    }
  }

  environment {
    IMAGE_NAME = "spring-petclinic"
    DOCKERHUB_USER = "prankumar313"  // Change to your Docker Hub username
  }

  stages {
    stage('Install Docker CLI') {
      steps {
        sh '''
          apt-get update
          apt-get install -y docker.io
          docker --version
        '''
      }
    }

    stage('Checkstyle') {
      when {
        expression { env.BRANCH_NAME != 'main' }
      }
      steps {
        sh './gradlew checkstyleMain checkstyleTest'
        archiveArtifacts artifacts: '**/build/reports/checkstyle/*.xml', allowEmptyArchive: true
      }
    }

    stage('Test') {
      when {
        expression { env.BRANCH_NAME != 'main' }
      }
      steps {
        sh './gradlew test'
      }
    }

    stage('Build (No Tests)') {
      when {
        expression { env.BRANCH_NAME != 'main' }
      }
      steps {
        sh './gradlew build -x test'
      }
    }

    stage('Build & Push Docker Image') {
      steps {
        script {
          COMMIT = sh(script: "git rev-parse --short HEAD", returnStdout: true).trim()
          IMAGE_TAG = "${DOCKERHUB_USER}/${env.BRANCH_NAME == 'main' ? 'main' : 'mr'}:${COMMIT}"

          sh """
            docker build -t ${IMAGE_TAG} .
          """

          withCredentials([usernamePassword(credentialsId: 'dockerhub-creds', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
            sh """
              echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
              docker push ${IMAGE_TAG}
            """
          }
        }
      }
    }
  }

  post {
    always {
      cleanWs()
    }
  }
}
