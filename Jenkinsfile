pipeline {
  agent {
    kubernetes {
      defaultContainer "maven"
      yamlFile 'jenkins.k8s.yaml'
    }
  }
  stages {
    stage('Compile') {
      steps {
          shell './mvnw -B package'
      }
    }
  }
}