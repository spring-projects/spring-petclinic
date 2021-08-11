pipeline {
  environment {
    registry = "thorak2001/spring-petclinic"
    registryCredential = 'dockerhub'
    dockerImageTag = 'latest'
    dockerImage = ''
  }
  agent {
    kubernetes {
      defaultContainer 'jnlp'
      yamlFile 'jenkins-slave.yaml'
    }
  }
  stages {
    stage('CHECKOUT') {
      steps {
        container('toolbox') {
          checkout scm
        }
      }
    }
    stage('BUILD') {
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
    stage('CREATE ARTIFACT') {
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
    stage('DEPLOY CI') {
      when {
        branch 'dev'
      }
      steps {
        container('toolbox') {
          script {
            sh """
              helm repo add app-chart https://thorak007.github.io/app-chart
              helm repo update
              helm upgrade app-release-dev app-chart/mychart --set deploy.containerTag=${dockerImageTag} -n dev
            """
          }
        }
      }
    }
    stage('DEPLOY QA') {
      when {
        branch 'prod'
      }
      steps {
        container('toolbox') {
          script {
            sh """
              helm repo add app-chart https://thorak007.github.io/app-chart
              helm repo update
              helm upgrade app-release-prod app-chart/mychart --set deploy.containerTag=${dockerImageTag} -n prod
            """
          }
        }
      }
    }
  }
}
