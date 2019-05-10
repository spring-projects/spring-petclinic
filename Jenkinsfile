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
        sh 'scp -P 2225 -r script.sh admin@192.168.0.20:/home/admin/.'
        sh 'ssh -p 2225 admin@192.168.1.19 < script.sh'
      }
    }
  }
}
