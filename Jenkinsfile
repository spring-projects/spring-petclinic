pipeline {
  agent {
    kubernetes {
      label 'spring-petclinic-demo'
      defaultContainer 'jnlp'
    }
  }
  stages {
    stage('checkout') {
      steps {
        checkout([
          $class: 'GitSCM', 
          branches: [[name: '*/main']], 
          userRemoteConfigs: [[url: 'https://github.com/thorak007/spring-petclinic.git']]
        ])
      }
    }
    stage('build') {
      steps {
        echo 'Starting to build docker image'
        script {
          def app = docker.build("thorak2001/spring-petclinic:${env.BUILD_ID}")
        } 
      }
    }
  }
}

