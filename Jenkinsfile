pipeline {
  agent any
  stages {
    stage('build') {
      steps {
        sh '''mvn clean install spring-boot:repackage
'''
      }
    }

    stage('test') {
      steps {
        echo 'test'
      }
    }

  }
}