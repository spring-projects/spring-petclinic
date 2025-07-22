pipeline {
    agent any

    environment {
       MAVEN_HOME = '/usr/share/maven'
       SONARQUBE = 'SonarQubeServer'
       ARGOCD_SERVER = 'https://argocd.example.com'
       ARGOCD_TOKEN = credentials('argocd-token')
    }

    stages {
        stage('Checkout') {
            steps{
                git url: 'https://github.com/spring-projects/spring-petclinic.git'
             }
         }
         
         stage('Build') {
             steps {
                 sh 'mvn clean compile'
             }
         }
 
         stage('Unit Tests') {
             steps {
                 sh 'mvn test'
                 junit 'target/surefire-reports/*.xml'
             }
          }

          stage('SonarQube Analysis') {
              steps {
                  withSonarQubeEnv('SonarQubeServer') {
                      sh 'mvn sonar:sonar'
                  }
              }
           }

           stage('Package') {
               steps {
                   sh 'mvn package'
               }
            }

             stage('Helm Deploy to Test') {
                steps {
                    sh '''
                      helm upgrade --install petclinic ./helm-chart\
                      --namespace test \
                      --set image.tag=latest
                    '''
                 }
              }
               
              stage('User Acceptance Tests') {
                  steps {
                      sh 'mvn verify -Pselenium'
                  }
              }

              stage('Promote to Production') {
                  steps {
                      sh '''
                        curl -X POST ${ARGOCD_SERVER}/api/v1/applications/petclinic/sync \
                        -H "Authorization: Bearer ${ARGOCD_token}" \
                        -H "Content-Type: application/json"
                      '''
                   }
               }
           }
        }
                 

