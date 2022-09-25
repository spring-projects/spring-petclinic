node('jdk-11-mvn') {
    
stage('vcs') {
    git branch: 'main', credentialsId: 'SSH_MY_LAPTOP', url: 'https://github.com/spring-projects/spring-petclinic.git'
             }
stage('build') {
    sh 'mvn package'
               }
stage('archive') {
    junit '**/surefire-reports/*.xml'
                 }
}
