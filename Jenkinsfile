node('OPENJDK11-MVN') {
    stage('vcs') {
    git branch: 'REL_INT_1.0', url: 'https://github.com/Moez786/spring-petclinic.git'
    }
    stage("build"){
        sh '/opt/apache-maven-3.8.6/bin/mvn package'
    }
    stage("Junit"){
        junit '**/surefire-reports/*.xml'
    }
}