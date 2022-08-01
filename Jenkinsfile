pipeline {
  agent {
    kubernetes {
      yamlFile 'jenkins.k8s.yaml'
    }
  }
  stages {
    container('maven') {
        stage('Run maven') {
            steps {
                sh 'mvn -version'
                sh "./mvnw -B package"
            }
        }
    }
  }
}