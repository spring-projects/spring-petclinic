pipeline {
    agent any
    stages {
       stage("BuildCode"){
          steps {
                sh """
        
                   ./mvnw package
        
                """
            }
        }

         stage("RunTests"){
             steps {
                 sh """
          
                     ./mvnw test
          
                 """
                }
          }
    } 
} 