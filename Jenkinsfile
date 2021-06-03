node {
    stage ('git springpetclinic') {
        //git clone
        git branch: 'Dev', url: 'https://github.com/Srinath246/spring-petclinic.git'
    }
    stage ('package the spring') {
        //maven package 
        sh 'mvn package'
    }
    stage ('archival') {
        //archiving artifactory
        archive 'target/*.jar'
    }
    stage ('test result') {
        junit 'target/surefire-reports/*.xml'
    }

}