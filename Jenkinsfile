pipeline {
    agent any
    
     stages {
         stage('Build'){
             steps {
                 git url: 'https://github.com/saisrinisrinivas/spring-petclinic.git'
             }

         }
         stage('Run'){
             steps {
                 sh 'mvn package'
             }
         }
     }
}
    
