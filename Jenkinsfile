pipeline{
    agent{label 'java'}
    tools {
        maven 'maven 3.9.4'
    }
    stages{
        stage('VCS'){
            steps{
                   git url: 'https://github.com/Cloud-and-devops-notes/spring-petclinic-jenkins.git',
                       branch: 'spcbranch'
            }
        }
        // stage('nexus'){
        //     steps{
        //         nexusArtifactUploader artifacts: [[artifactId: 'spring-petclinic', classifier: '', file: '/home/ubuntu/spring-petclinic/target/spring-petclinic-3.1.0-SNAPSHOT.jar', type: 'jar']], credentialsId: 'nexus', groupId: 'org.springframework.samples', nexusUrl: '54.237.114.152:8081', nexusVersion: 'nexus3', protocol: 'http', repository: 'maven-snapshots', version: '3.1.0-SNAPSHOT'
        //     }
        // }    
        stage('Build & SonarQube Scan') {
            steps {
                sh 'ls'
                // sh 'cd spring-petclinic-jenkins'
                sh 'mvn --version'
                sh 'mvn package'
            //   withSonarQubeEnv('SONAR_CLOUD') {
            //         sh 'mvn clean install sonar:sonar -Dsonar.organization=qtdevopssohail123 -Dsonar.token=8c15adacf466a5ccd721f4f7cdb2c4bf17df84ab -Dsonar.projectKey=qtdevopssohail123'
               }
            }
        }
    }






































// pipeline{
//     agent{label 'java-17'}
//     stages{
//         stage('VCS'){
//             steps{
//                    git credentialsId: 'GIT_HUB_CREDENTIALS',
//                        url: 'https://github.com/Cloud-and-devops-notes/spring-petclinic-jenkins.git',
//                        branch: 'main'
//             }
//         }
//         stage('artifact build'){
//           steps{
//             sh 'docker image build -t spc-mvn .'
//             sh 'docker image list'

//           } 
//         } 
//         stage('artifact') {
//            steps {
//             archiveArtifacts artifacts: '**/target/spring-petclinic-3.1.0-SNAPSHOT.jar',
//                          onlyIfSuccessful: true
//             junit testResults: '**/surefire-reports/TEST-*.xml'
//            }
//         }
        
//         stage('docker login'){
//             steps{   
//         withCredentials([string(credentialsId: 'DOCKER_HUB_PASSWORD',variable: 'PASSWORD')]) {
//          sh 'docker login -u sridhar006 -p $PASSWORD'  
//          }
//             }
//         }
//         stage('docker push image '){
//             steps{
//                 sh 'docker image tag spc-mvn sridhar006/spc-mvn:${BUILD_ID}'
//                 sh 'docker push sridhar006/spc-mvn:${BUILD_ID}'
                
//             }
//         }        
//         stage("kubernetes deployment"){
//            steps{ 
//           sh 'kubectl apply -f deployement.yaml'
//       }

//       }  

//      }
//  }
