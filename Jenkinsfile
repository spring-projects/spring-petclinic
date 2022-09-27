pipeline {
    agent any
     stages {
        stage('VCS') {
         steps {
            git branch: 'Spring_test', url: 'https://github.com/ravibandanadham/spring-petclinic.git'
         }
       stage('Build') {
         steps {
        sh 'mvn package'
         }
       }
      stage('Test') {
      steps {
           junit '**/target/*.xml'
      }

      }
        }

     }
}