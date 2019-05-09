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
       // sh ''
        sh 'mvn deploy'
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
