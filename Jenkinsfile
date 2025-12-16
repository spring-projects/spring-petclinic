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
    } 
} 