#!groovy

pipeline {
  agent none
  stages {
    stage('Maven Install') {
      agent {
        docker {
          image 'maven:3.5.4-jdk-8-alpine'
          args '-u root'
        }
      }
      steps {
        sh 'mvn clean install'
      }
    } 
    }
}
