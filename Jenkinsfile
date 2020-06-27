#!groovy

pipeline {
  agent none
  stages {
    stage('Maven Install') {
      steps {
        mvn clean install -Dmaven.test.skip=true
      }
    }
    stage('Run tests') {
      agent any
      steps {
        mvn test
      }
    }
  }
}
