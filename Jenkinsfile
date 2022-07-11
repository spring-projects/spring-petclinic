#!groovy
pipeline {
    agent none
   stages {     
    stage('Maven Install') {
      agent {         
       docker {          
         image 'maven:3.5.0'         
     }       
  }       
  steps {
       sh 'mvn clean install'
       }
     }
       stage('Docker Build') {
      agent {
    docker {
      image 'aos-bint.corp.apple.com/base/ol-jdk11-all'
    }
    }
   }
 }
