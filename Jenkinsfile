pipeline {
  agent {
    kubernetes {
      label 'spring-petclinic-demo'
      defaultContainer 'jnlp'
      yaml """
apiVersion: v1
kind: Pod
metadata:
labels:
  component: ci
spec:
  serviceAccountName: jenkins
  containers:
  - name: docker
    image: docker:latest
    command:
    - cat
    tty: true
    volumeMounts:
    - mountPath: /var/run/docker.sock
      name: docker-sock
  volumes:
    - name: docker-sock
      hostPath:
        path: /var/run/docker.sock
"""
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
        container ('docker') {
          echo 'Starting to build docker image'
          script {
            def app = docker.build("thorak2001/spring-petclinic:${env.BUILD_ID}")
          } 
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
