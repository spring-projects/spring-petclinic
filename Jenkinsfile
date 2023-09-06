pipeline{
    agent{label 'dotnet-7'}
    stages{
        stage('VCS'){
            steps{
                   git credentialsId: 'GIT_HUB_CREDENTIALS',
                       url: 'https://github.com/Cloud-and-devops-notes/spring-petclinic-jenkins.git',
                       branch: 'tera'


            }
        }
        stage('artifact build'){
          steps{
            sh 'sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys B53DC80D13EDEF05'
            sh 'sudo apt upgrade dotnet-sdk-7.0 -y'
            sh 'dotnet restore src/NopCommerce.sln'
            sh 'dotnet build -c Release src/NopCommerce.sln'
            sh 'dotnet publish -c Release src/Presentation/Nop.Web/Nop.Web.csproj -o publish'
            sh 'sudo apt install zip -y'
            sh 'zip -r nopCommerce.zip publish'
            archive '**/nopCommerce.zip'
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
                sh 'docker image build -t nop123 .'
                sh 'docker image tag nop123 sridhar006/nopaugest:${BUILD_ID}'
                sh 'docker push sridhar006/nopaugest:${BUILD_ID}' 
                
    }
        }
        stage("kubernetes deployment"){
           steps{ 
           sh 'kubectl apply -f deployment.yaml'
      }

      }  

     }
 }
