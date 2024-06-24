#!groovy
pipeline {
    agent none
   stages {     
    stage('Maven compile') {
      agent {         
       docker {          
         image 'maven:3.9.7'         
     }       
  }       
  steps {
       sh 'mvn clean compile'
       }
     }
   }
 }
