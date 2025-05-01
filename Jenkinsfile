pipeline {
  agent none

  environment {
    DOCKERHUB_USER = "prankumar313"
  }

  stages {
    stage('Checkstyle') {
      agent {
        docker {
          image 'gradle:8.1.1-jdk17'
        }
      }
      when {
        expression { env.BRANCH_NAME != 'main' }
      }
      steps {
        sh './gradlew checkstyleMain checkstyleTest'
        archiveArtifacts artifacts: '**/build/reports/checkstyle/*.xml', allowEmptyArchive: true
      }
    }

    stage('Test') {
      agent {
        docker {
          image 'gradle:8.1.1-jdk17'
        }
      }
      when {
        expression { env.BRANCH_NAME != 'main' }
      }
      steps {
        sh './gradlew test'
      }
    }

    stage('Build (No Tests)') {
      agent {
        docker {
          image 'gradle:8.1.1-jdk17'
        }
      }
      when {
        expression { env.BRANCH_NAME != 'main' }
      }
      steps {
        sh './gradlew build -x test'
      }
    }

    stage('Build & Push Docker Image') {
      agent { label 'docker-enabled' }  // runs on a node with Docker installed
      steps {
        script {
          def commit = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
          def repo = env.BRANCH_NAME == 'main' ? 'main' : 'mr'
          def image = "${DOCKERHUB_USER}/${repo}:${commit}"

          sh "docker build -t ${image} ."

          withCredentials([usernamePassword(credentialsId: 'dockerhub-creds', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
            sh """
              echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
              docker push ${image}
            """
          }
        }
      }
    }
  }
}
