pipeline {
  agent any
  stages {
    stage('build') {
      steps {
        sh './mvnw clean package -Dmaven.test.skip=true '
//         sh 'docker --version'
//         sh 'docker build -f . --tag pet .'
      }
    }

    stage('sonar') {
      steps {
        withSonarQubeEnv(installationName:'sonar-1')
        {
        sh './mvnw sonar:sonar -Dsonar.java.binaries=target/classes  -Dsonar.login=admin -Dsonar.password=password'
        }
      }
    }

    stage('run') {
      steps {
        sh 'cp -r target/ /shared/target'
      }
    }

  }
}
