pipeline {
  agent any
  stages {
    stage ('Checkout') {
      steps {
        git branch: 'main', url: 'https://github.com/gjraju1304/spring-petclinic.git'
      }
    }
    stage ('Build') {
      steps {
        tools {
            maven 'Maven-3.9.5'
        }
        sh "mvn clean install"
      }
    }
  }
}
