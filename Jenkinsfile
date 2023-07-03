pipeline {
    agent any
    
     stages {
         stage('GitCheckout'){
             steps {
                 git 'https://github.com/saisrinisrinivas/spring-petclinic.git'
             }

         }
         stage('Run'){
             steps {
                 sh 'mvn package'
             }
         }
     }
}
    
