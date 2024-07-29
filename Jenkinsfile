node('Open-JDK8-8') {
    stage('Versioncontrol') {
      git branch: 'REL_INT_1.0', url: 'https://github.com/thippareddy03/spring-petclinic.git'
    }
    stage('BuildPackage') {
      sh '/usr/share/maven/bin/mvn package'
    }
    stage('unittest') {
      junit '**/surefire-reports/*.xml'
    }
}
