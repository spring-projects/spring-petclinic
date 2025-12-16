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

                    ./mvnw clean verify sonar:sonar \
                    -Dsonar.projectKey=spring-petclinic \
                    -Dsonar.projectName=spring-petclinic

                """
                }
            }
        }

         stage("QualityGate"){
             steps {
                    timeout(time: 1, unit: 'MINUTES') {
                        waitForQualityGate abortPipeline: true
                    }
                }
            }
 
         stage('Upload Artifacts') {
             when {
                 expression { currentBuild.result == null || currentBuild.result == 'SUCCESS' }
            }
             steps {
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }

            
    }
            
}
        

    
