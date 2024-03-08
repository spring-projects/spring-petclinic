pipeline {
  agent any
  tools {
    JDK 'jdk17'
    maven 'M3'
  }
  
  stages {
    stage('Git clone') {
      steps {
        echo 'Git clone'
        git url: 'https://github.com/lwj9812/spring-petclinic.git',
          branch: 'efficien-webjars'
      }
    }
  }
}
