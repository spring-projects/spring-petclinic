#!/bin/env groovy
pipeline {
  agent none

  stages {

    stage('Deploy to Artifactory') {
      agent  {
        node {
          label 'tester'
        }
      }
      steps {
        sh 'mvn deploy:deploy'
      }
    }

    stage('Deploy to QA') {
      agent {
        node {
          label 'tester'
        }
      }
      steps {
        sh 'scp -P 2223 -r script.sh admin@192.168.1.19:/home/vagrant/.'
        sh 'ssh -p 2223 admin@192.168.1.19 < script.sh'
      }
    }
  }
}
