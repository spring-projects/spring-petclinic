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
        sh "./mvnw sonar:sonar -Dsonar.host.url=http://devops1-sonarqube-1:9000 -Dsonar.login=admin -Dsonar.password=password"
      }
    }

    stage('run') {
      steps {
        sh 'cp -r target/ /shared/target'
        sh 'JENKINS_NODE_COOKIE=dontKillMe nohup java -jar /shared/target/*.jar &'

      }
    }

  }
}
