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
        sh "mvn clean install"
      }
    }
  }
}
