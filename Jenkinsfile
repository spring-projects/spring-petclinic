pipeline {
  agent {
     docker {
       image 'gradle:8.1.1-jdk17'
       args '-v /var/run/docker.sock:/var/run/docker.sock'
     }
  }
  agent any
  environment {
    DOCKER_IMAGE = "prankumar313"
    COMMIT = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
  }
  stages {
    stage('Install Docker') {
      steps {
        sh '''
         apt-get update
         apt-get install -y docker.io
        '''
      }
    }
    stage('Checkstyle') {
      when {
        not { branch 'main' }
      }
      steps {
        sh './gradlew checkstyleMain checkstyleTest'
        archiveArtifacts artifacts: '**/build/reports/checkstyle/*.xml', allowEmptyArchive: true
      }
    }

    stage('Test') {
      when {
        not { branch 'main' }
      }
      steps {
        sh './gradlew test'
      }
    }

    stage('Build (No Tests)') {
      when {
        not { branch 'main' }
      }
      steps {
        sh './gradlew build -x test'
      }
    }

    stage('Build Docker Image') {
      steps {
        script {
          def tag = BRANCH_NAME == 'main' ? 'latest' : "${COMMIT}"
          def repo = BRANCH_NAME == 'main' ? "${DOCKER_IMAGE}-main" : "${DOCKER_IMAGE}-mr"
          sh "docker build -t ${repo}:${tag} ."
          withCredentials([usernamePassword(credentialsId: 'dockerhub', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
            sh "echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin"
            sh "docker push ${repo}:${tag}"
          }
        }
      }
    }
  }
}
