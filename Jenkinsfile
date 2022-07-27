pipeline {
    agent any
   tools{
        maven "maven3"
    }
   stages {  
       
       
    stage('Maven Install') {       
      steps {
       sh 'mvn clean install'
       }
     }
   }
 }
