pipeline {
  agent {
    kubernetes {
      defaultContainer "maven"
      yamlFile "jenkins.k8s.yaml"
    }
  }
  tools { 
        maven 'Maven 3.8.6'
  }
  stages {
    
    stage("Build") {
      steps {
          sh "mvn clean package -DskipTests=true"
      }
    }

    stage("Test") {
      steps {
          sh "mvn test"
      }
      post {
        always {
            junit 'target/surefire-reports/*.xml' 
        }
      }
    }

    stage("SonarQube") {
      steps {
          withCredentials([string(credentialsId: 'sonar', variable: 'SONAR')]) {
            sh "mvn sonar:sonar -Dsonar.login=${SONAR} -Dsonar.host.url=https://sonarcloud.io"
          }
      }
    }

  }
}