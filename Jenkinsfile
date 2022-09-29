node('jdk-11-mvn') {
    stage('vcs') {
    git branch: 'google', credentialsId: 'saiteja', url: 'https://github.com/vikasvarmadunna/spring-petclinic.git'
}
    stage('build') {
    sh 'mvn package'
}
       stage('archive') {
    archive 'target/spring-petclinic-*.jar'
    junit '**/surefire-reports/*.xml'
}

}
