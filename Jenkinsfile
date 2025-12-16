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

          stage("Sonar-Scanning") {
            steps {
                withSonarQubeEnv('sonarqube') {
                    sh """
                
                      ${tool 'SonarScanner'}/bin/sonar-scanner \
                      -Dsonar.projectKey=spring-petclinic \
                      -Dsonar.projectName=spring-petclinic\
                      -Dsonar.sources=. \
                      -Dsonar.sourceEncoding=UTF-8
                    """
                }
            }
          }
          
    } 
} 