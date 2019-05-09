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
        sh 'mvn test'
      }
    }
    stage('Deploy to Artifactory') {
      agent  {
        node {
          label 'tester'
        }
      }
      steps {
        sh 'maven deploy'
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
