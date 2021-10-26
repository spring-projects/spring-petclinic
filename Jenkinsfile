pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        sh './mvnw package'
      }
    }
    stage('Sonarqube Test') {
        environment {
            scannerHome = tool 'sq_a1'
        }
        steps {
          
            withSonarQubeEnv('sq_a1') {
                sh "${scannerHome}/bin/sonar-scanner "+
                    "-Dsonar.projectKey=petclinic " +
                    "-Dsonar.projectName=spring-petclinic " +
                    "-Dsonar.projectVersion=1.0.0 " +
                    "-Dsonar.java.binaries=/var/lib/jenkins/workspace/spring-petclinic_main/src " +
                    "-Dsonar.sourceEncoding=UTF-8"
            }
        }
    }
  }
}
