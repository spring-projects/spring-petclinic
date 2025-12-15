pipeline {
    agent any
    stages {
       stage("BuildCode"){
          steps {
                sh """
                   cd spring-petclinic
                   ./mvnw package
                   java -jar target/*.jar
                """
            }
        }
    } 
} 