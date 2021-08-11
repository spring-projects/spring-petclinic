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
            sh """ 
              docker build --network=host -t ${registry}:${env.BUILD_NUMBER} .
            """
          }
        }
      }
    }
    stage('Deploy Image') {
      steps {
        container('toolbox') {
          script {
            docker.withRegistry( '', registryCredential ) {
              sh """
                docker push "${registry}:${env.BUILD_NUMBER}"
              """
            }
          }
        }
      }
    }
  }
}

