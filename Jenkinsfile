#!groovy
pipeline {
	agent none
  stages {
  	stage('Maven Install') {
        agent any
      steps {
      	sh 'mvn clean install'
      }
    }
  }
}
