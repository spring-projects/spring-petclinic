pipeline {
  environment {
    registry = "thorak2001/spring-petclinic"
    registryCredential = 'dockerhub'
    dockerImage = ''
  }
  agent {
    kubernetes {
      defaultContainer 'jnlp'
      yamlFile 'jenkins-slave.yaml'
    }
  }
  stages {
    stage('Cloning Git') {
      steps {
        container('toolbox') {
          checkout scm
        }
      }
    }
    stage('Building image') {
      steps {
        container('toolbox') {
          script {
            dockerImage = docker.build registry + ":$BUILD_NUMBER"
          }
        }
      }
    }
    stage('Deploy Image') {
      steps {
        container('toolbox') {
          script {
            docker.withRegistry( '', registryCredential ) {
              dockerImage.push()
            }
          }
        }
      }
    }
  }
}
