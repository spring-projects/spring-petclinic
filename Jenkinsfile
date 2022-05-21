// node('JDK11') {
//     stage('Source Code') {
//         git branch: 'scripted', url: 'https://github.com/pixelswapnil13/spring-petclinic.git'
//     }
//     stage('Build the code') {
//          sh 'mvn package'
//     }
//     stage('Junit Test') {
//          junit '**/surefire-reports/*.xml'
//     }
//     stage('Archive artifact'){
//         archiveArtifacts artifacts: '**/*.jar', followSymlinks: false
//     }
// }

pipeline {
 agent{ label 'JDK11' }
 triggers {
        cron('0 * * * *')
    }
 stages{
    stage('Source Code') {
       steps{
           git branch: 'declarative', url: 'https://github.com/pixelswapnil13/spring-petclinic.git' 
        }
    }
    stage('Build the code'){
        steps{
            sh 'mvn clean package'
        }
    }
    stage('Junit Test'){
        steps{
            junit '**/surefire-reports/*.xml'
        }
    }
    stage('Archive artifact'){
        steps{
           archiveArtifacts artifacts: '**/*.jar', followSymlinks: false
        }
    }
  }
}
