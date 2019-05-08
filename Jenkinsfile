#!/bin/env groovy
pipeline {
  agent none

  stages {

    stage('Test') {
      agent {
        node {
          label 'tester'
        }
      }
      steps {
        sh ''
      }
    }
    stage('Deploy to Artifactory') {
      agent  {
        node {
          label 'tester'
        }
      }
      steps {
        sh ''
      }
    }

    stage('Deploy to QA') {
      agent {
        node {
          label 'tester'
        }
      }
      steps {
        sh ''
      }
    }
  }
}
