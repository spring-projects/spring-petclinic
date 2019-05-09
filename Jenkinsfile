#!/bin/env groovy
pipeline {
  agent any 

  stages {

    stage('Test') {
      /*
      agent {
        node {
          label 'tester'
        }
      }
      */
      steps {
        sh 'mvn deploy'
      }
    }
    stage('Deploy to Artifactory') {
      /*
      agent  {
        node {
          label 'tester'
        }
      }
      */
      steps {
       // sh ''
	echo 'NOT YET IMPLEMENTED'
      }
    }

    stage('Deploy to QA') {
      /*
      agent {
        node {
          label 'tester'
        }
      }
      */
      steps {
        //sh ''
	echo 'NOT YET IMPLEMENTED'
      }
    }
  }
}
