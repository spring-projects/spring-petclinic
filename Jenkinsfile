pipeline {
  agent 'agent1' 

  environment {
    imageName = "spring-pet-clinic"
    registryCredentials = "nexus-credentials"
    registry = "https://localhost:9081"
    dockerImage = ''
  }

  tools {
    maven 'm3'
  }

  stages {
    stage ('Docker build') {
      steps {
        script {
          dockerImage = docker.build(imageName)
        }
      }

    }
    stage ('Build') {
      steps {
        sh './mvnw -B -DskipTests clean package'
      }
    }

    stage ('Test') { 
      steps {
        sh './mvnw test'
      }
    }

    stage ('Deploy') {
      steps {
        sh 'echo "hello $USER"'
      }
    }

  }
}
