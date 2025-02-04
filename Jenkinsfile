pipeline {
  agent any
  tools {
    maven 'm3'
  }
  stages {
    stage ('Build') {
      steps {
        sh './mvnw -B -DskipTests clean package'
      }
    }

    stage ('Test') { 
      steps {
        sh './mvnw test'
      }
    }

    stage ('Deploy') {
      steps {
        sh 'java -jar ./target/*.jar'
      }
    }

}
