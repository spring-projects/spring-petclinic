pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        sh './mvnw clean install'
      }
    }
    
    stage('Test') {
            steps {
                sh './mvnw test' 
            }
     }
      
      stage('Package') {
            steps {
                sh './mvnw package' 
            }
        }

      stage('Deploy') {
            steps {
                sh './mvnw deploy 
            }
        }
  }
}