node('JDK-11-MVN') {
    stage('Git'){
        git branch: 'main', url: 'https://github.com/ShaikNasee/spring-petclinic.git'
    }
    stage('build'){
        sh '''
           echo "PATH=${PATH}"
           echo "M2_HOME=${M2_HOME}"  
        '''
    }
    stage('Archive artifacts'){
        archiveArtifacts artifacts: 'target/*.jar', followSymlinks: false
    }
    stage('publish test results'){
        junit 'target/surefire-reports/*.xml'
    }
}