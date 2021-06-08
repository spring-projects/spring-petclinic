pipeline {
  agent slave1
  stages {
    stage ('Build') {
      steps {
        echo 'Running build automation'
        sh './mvnw package'
        sh 'java -jar target/*.jar'
      }
    }
  }
}
