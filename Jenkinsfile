pipeline {
  agent { node { label 'ubuntu' } }
  stages {
    stage ('git') {
      steps {
      git url: 'https://github.com/Kiranteja623/spring-petclinic.git'
          branch: 'main'
    }
    }
    stage ('package') {
      steps {
          sh 'mvn package'
    }
    }
    stage ('sonarqube') {
        steps {
          withSonarQubeEnv('Kiranteja623') {
                    sh 'mvn clean package sonar:sonar -Dsonar.organization=kiranteja623 -Dsonar.login=ddfb007e5e67490bc157bd464d92bdfd2690f1d2 -Dsonar.host.url=https://sonarcloud.io -Dsonar.projectKey=kiranteja623/petclinic'
                }
        }
  }
}
}
