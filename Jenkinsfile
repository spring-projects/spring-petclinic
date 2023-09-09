pipeline{
    agent{label 'java-17'}
    stages{
        stage('VCS'){
            steps{
                   git credentialsId: 'GIT_HUB_CREDENTIALS',
                       url: 'https://github.com/Cloud-and-devops-notes/spring-petclinic-jenkins.git',
                       branch: 'main'
            }
        }
        stage('artifact build'){
          steps{
            sh 'docker image build -t spc-mvn .'
            sh 'docker image list'

          } 
        } 
        stage('artifact') {
           steps {
            archiveArtifacts artifacts: '**/target/spring-petclinic-3.1.0-SNAPSHOT.jar',
                         onlyIfSuccessful: true
            junit testResults: '**/surefire-reports/TEST-*.xml'
           }
        }
        
        stage('docker login'){
            steps{   
        withCredentials([string(credentialsId: 'DOCKER_HUB_PASSWORD',variable: 'PASSWORD')]) {
         sh 'docker login -u sridhar006 -p $PASSWORD'  
         }
            }
        }
        stage('docker push image '){
            steps{
                sh 'docker image tag spc-mvn sridhar006/spc-mvn:${BUILD_ID}'
                sh 'docker push sridhar006/spc-mvn:${BUILD_ID}'
                
            }
        }        
        stage("kubernetes deployment"){
           steps{ 
           sh """
           
           """
           sh 'kubectl apply -f deployement.yaml'
      }

      }  

     }
 }
