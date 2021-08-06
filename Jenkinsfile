pipeline {
  agent any
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
    stage('create artifact') {
      steps {
        echo 'push image to Docker hub'
      }
    }
    stage('Deploy to CI') {
      when {
        branch 'dev'
      }
      steps {
        echo 'deploy to CI env'
      }
    }
    stage('Deploy to QA') {
      when {
        branch 'prod'
      }
      steps {
        echo 'deploy to QA env'
      }
    }
  }
}
