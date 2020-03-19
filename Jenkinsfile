pipeline {
  agent any
  stages {
    
    stage('Build') {
      steps {
        sh 'mvn clean'
      }
    }

        stage('Build') {
      steps {
        sh 'mvn test'
      }
    }
    
        stage('Build') {
      steps {
        sh 'mvn package'
      }
    }
    
        stage('Build') {
      steps {
        sh 'mvn deploy'
      }
    }
    
  }
}
