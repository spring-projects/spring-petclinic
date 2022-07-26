pipeline {
    agent any
   stages {     
    stage('Maven Install') {          
  steps {
       sh 'mvn clean install'
       }
     }
       stage('Docker Build') {
      agent any
      steps {
        sh 'docker build -t amar1doc/spring-petclinic:latest .'
      }
    }
   }
 }
