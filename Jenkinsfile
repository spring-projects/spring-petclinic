node('jdk11-mvn3.8.5') {
    stage('SourceCode') {
        // get the code from git repo on the branch sprint1_develop
        git branch: 'scripted', url: 'https://github.com/SriSuryaTej/spring-petclinic.git'
    }


    stage('Archiving and Test Results') {
        junit '**/surefire-reports/*.xml'
        archiveArtifacts artifacts: '**/*.jar', followSymlinks: false
    }

}