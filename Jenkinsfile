pipeline {
  agent any
  stages {
    stage('show work directory') {
      steps {
        sh 'cp /home/ubuntu/pom.xml /var/lib/jenkins/workspace/spring-petclinic_pet-project'
      }
    }

    stage('compile') {
      steps {
        sh 'mvn compile'
      }
    }

    stage('test') {
      steps {
        sh 'mvn test'
      }
    }

    stage('build app') {
      steps {
        sh 'mvn package -Dmaven.test.skip=true'
      }
    }

    stage('release') {
      steps {
        sh './mvnw release:prepare'
      }
    }

  }
}