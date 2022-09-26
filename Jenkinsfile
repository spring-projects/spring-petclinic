node('REDHAT-NODE') {
    stage('vcs') {
        git branch: 'ppm', url: 'https://github.com/Nagaraju11111/spring-petclinic.git'
    }
    stage('build') {
        sh '/usr/share/maven/bin/mvn package'
    }
    stage('Archive JUnit formatted test results') {
        junit '**/surefire-reports/*.xml'
    }
  stage('Archive artifacts') {
    archive '**/target/*.jar'
  }
}
