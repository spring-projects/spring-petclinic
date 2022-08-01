pipeline {
  agent {
    kubernetes {
      yamlFile 'jenkins.k8s.yaml'
    }
  }
  stages {
    stage('Restore') {
      steps {
        container('maven') {
          sh 'mvn validate'
        }
      }
    }

     stage('Assemble') {
      steps {
        container('maven') {
          sh 'mvn assemble'
        }
      }
    }
  }
}