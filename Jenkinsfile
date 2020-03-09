pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        sh 'mvn spring-javaformat:apply'
        sh './mvnw package'
      }
    }

    stage('Test') {
      steps {
        sh 'mvn clean test'
      }
    }

    stage('Package') {
      steps {
        echo 'Package'
      }
    }

    stage('Deploy') {
      when {
        branch 'master'
      }
      steps {
        echo 'Deploying'
      }
    }

  }
}