node('JDK11') {
    stage('Source Code') {
        git branch: 'scripted', url: 'https://github.com/pixelswapnil13/spring-petclinic.git'
    }
    stage('Build the code') {
         sh 'mvn package'
    }
    stage('Junit Test') {
         junit '/surefire-reports/*.xml'
    }
    stage('Archive artifact'){
        archiveArtifacts artifacts: '**/*.jar', followSymlinks: false
    }
}