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
        git(url: 'https://github.com/NikosNS/spring-petclinic.git', branch: 'pet-project', credentialsId: 'git')
        sh 'git config --global credential.helper cache'
        sh 'git push --set-upstream springpet pet-project'
        sh './mvnw release:prepare'
      }
    }

  }
}