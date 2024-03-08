pipeline {
  agent any
  tools {
    jdk 'jdk17'
    maven 'M3'
  }
  
  stages {
    stage('Git clone') {
      steps {
        echo 'Git clone'
        git url: 'https://github.com/lwj9812/spring-petclinic.git',
          branch: 'efficient-webjars'
      }
    }
  }
  post {
      success {
          echo 'Git Clone Success!!'
      }
      failure {
          echo 'Git Clone Fail'
      }
  }

}
