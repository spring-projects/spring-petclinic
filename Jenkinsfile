pipeline {
  agent any
  stages {
    stage('build') {
      steps {
        sh './mvnw clean package -Dmaven.test.skip=true '
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
        sh 'cp -r target/petclinic.jar bin/*.jar'
        sh 'java -jar bin/target_jar/*.jar'
//         sh 'nohup java -jar target/*.jar > petclinic.log 2>&1 &'
      }
    }

  }
}
