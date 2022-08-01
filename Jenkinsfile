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
      post {
        always {
            junit 'target/surefire-reports/**/*.xml' 
        }
      }
    }

    stage("Package") {
      steps {
          sh "mvn package -Dmaven.test.skip=true"
      }
    }
  }
}