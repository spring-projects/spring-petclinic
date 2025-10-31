#!groovy

pipeline {
  agent none
  stages {
    stage('Maven Install') {
      agent {
        docker {
          image 'maven:3.9-eclipse-temurin-25' 
          reuseNode true
        }
      }
      steps {
        sh 'mvn clean install'
      }
    }
    stage('Docker Build') {
        agent any
        steps {
            sh 'docker build -t jilopezv/spring-petelinic:latest .'
        }
    }
  }
}
