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
          sh "mvn dependency:copy-dependencies compile"
      }
    }

    stage("Test") {
      steps {
          sh "mvn dependency:copy-dependencies test"
      }
    }

    stage("Package") {
      steps {
          sh "mvn dependency:copy-dependencies package -Dmaven.test.skip=true"
      }
    }
  }
}