node('jdk11-mvn3.8.5') {
    stage('SourceCode') {
        // get the code from git repo on the branch sprint1_develop
        git branch: 'scripted', url: 'https://github.com/SriSuryaTej/spring-petclinic.git'
    }

    stage('Build the code') {
        sh 'mvn clean package'
    }

    stage('Archiving and Test Results') {
        junit '**/target/surefire-reports/*.xml'
        archiveArtifacts artifacts: '**/target/*.jar', followSymlinks: false
    }

}