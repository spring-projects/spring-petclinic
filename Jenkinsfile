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
            sh 'docker build . -t sridhar006/spc-jenkins:${BUILD_ID}'
            sh 'docker image list'
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
                sh 'docker image build -t spc123 .'
                sh 'docker image tag spc123 sridhar006/spc-jenkins:${BUILD_ID}'
                sh 'docker push sridhar006/spc-jenkins:${BUILD_ID}' 
                
    }
        }
        stage("kubernetes deployment"){
           steps{ 
           sh 'kubectl apply -f deployment.yaml'
      }

      }  

     }
 }
