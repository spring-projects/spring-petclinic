pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        bat(script: ' mvn clean install -Dmaven.test.skip=true', returnStatus: true, returnStdout: true)
      }
    }
  }
}
