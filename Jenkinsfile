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
        sh 'mvn deploy'
      }
    }

    stage('Deploy to QA') {
      agent {
        node {
          label 'tester'
        }
      }
      steps {
        sh 'scp -P 2223 -r script.sh vagrant@192.168.0.18:/home/vagrant/.'
        sh 'ssh -p 2223 vagrant@192.168.0.18 < script.sh'
      }
    }
  }
}
