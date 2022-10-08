node('JDK-11-MVN') {
    stage('Git'){
        git branch: 'main', url: 'https://github.com/ShaikNasee/spring-petclinic.git'
    }
    stage('build'){
        sh '''
           echo "PATH=${PATH}"
           echo "M2_HOME=${M2_HOME}" 
 
        '''
        sh '/usr/local/apache-maven-3.8.6/bin/mvn clean package '
    }
    stage('archive'){
        archiveArtifacts artifacts: 'target/*.jar', followSymlinks: false
    }
    stage('junit'){
        junit 'target/surefire-reports/*.xml'
    }

}