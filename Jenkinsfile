pipeline {

    agent any 

    stages {

        stage ("Build"){
            steps {
               sh "./mvnw install"     
            }
        }

        stage ("Run unit-test"){

            steps {
               sh "./mvnw test"
                           }
        }

        stage ("CodeScaning"){
            environment {
                SONAR_HOME = tool 'sonar-scan'
                  }
                  steps{
                    withSonarQubeEnv('SonarServer') {
                   sh '''$SCANNER_HOME/bin/sonar-scanner \
                       -Dsonar.projectKey=myPETC \
                       -Dsonar.projectName=mypetclinc \
                       -Dsonar.sources=. \
                       -Dsonar.java.binaries=target/classes \
                       -Dsonar.exclusions=src/test/java/****/*.java \
                       -Dsonar.analysis.mode=publish \
                       -Dsonar.projectVersion=${BUILD_NUMBER}-${GIT_COMMIT_SHORT}
                    
                    '''

                   
                       
                  }
                }
            
        }
    }
}
