node('JDK11-MVN') {
    stage('vcs') {
        git branch: 'REL_V1', url: 'https://github.com/ziyad-ansari/spring-petclinic.git'
    }
    stage("build") {
        sh 'mvn package'
    }
    stage("archive results") {
        junit '**/surefire-reports/*.xml'

    }
}