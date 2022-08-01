pipeline {
  agent {
    kubernetes {
      defaultContainer "maven"
      yamlFile "jenkins.k8s.yaml"
    }
  }
  stages {
    stage("Compile") {
      steps {
          sh "mvn clean install"
      }
    }
  }
}