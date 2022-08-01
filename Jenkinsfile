pipeline {
  agent {
    kubernetes {
      defaultContainer "maven"
      yamlFile "jenkins.k8s.yaml"
    }
  }
  stages {
    stage("Initialize") {
      steps {
          sh "mvn clean initialize"
      }
    }

    stage("Compile") {
      steps {
          sh "mvn compile"
      }
    }

    stage("Test") {
      steps {
          sh "mvn test"
      }
    }

    stage("Package") {
      steps {
          sh "mvn package"
      }
    }
  }
}