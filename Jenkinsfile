pipeline {
  agent {
    kubernetes {
      yamlFile 'jenkins.k8s.yaml'
    }
  }
  stages {
    stage('Compile') {
      steps {
        container('maven') {
          sh 'mvnw -B package'
        }
      }
    }
  }
}