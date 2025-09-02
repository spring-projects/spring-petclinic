pipeline {
  agent any

  tools {
    maven "M3"
    jdk "JDK17"
  }
  
  stages{
    stage('Git Clone'){
      steps {
        git url: 'https://github.com/wodnr533/spring-petclinic.git', branch: 'main'
      }
    }
    stage('Maven Build'){
      steps {
        sh 'mvn -Dmaven.test.failure.ignore=true clean package'
      }
    }  
  }
}
