pipeline {
  agent {
    docker { image 'maven:3.8.5-openjdk-17' }
  }

  stages {
    stage('Checkstyle') {
      steps {
        checkout scm
        sh 'mvn checkstyle:checkstyle'
      }
      post {
        always {
          archiveArtifacts artifacts: 'target/checkstyle-result.xml', allowEmptyArchive: true
        }
      }
    }
    stage('Build') {
      steps {
        sh 'mvn clean package -DskipTests'
      }
/*
      post {
        always {
          archiveArtifacts artifacts: 'target/*.jar', allowEmptyArchive: true
        }
      }
*/
    }
  }
}
