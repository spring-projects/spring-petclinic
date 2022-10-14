node('JDK-11-MVN3.8.6'){
    properties([pipelineTriggers([upstream('init-project, ')])])
    stage('source code '){
        git branch: 'scripted', url: 'https://github.com/ShaikNasee/spring-petclinic.git'
    }
    stage('mvn build'){
        sh '/usr/local/apache-maven-3.8.6/bin/mvn clean package'

    }
    stage('archive Artifacts'){
        archiveArtifacts artifacts: 'target/*.jar', followSymlinks: false
    }
    stage('pushing unit test reports'){
         junit '**/TEST-*.xml'
    }
    stage('docker image build '){
        sh 'docker image build --tag dockerimages:1.0 .'
        sh 'docker image ls'
    }
    stage('running the java appcation on docker '){
              sh 'scp deploy.sh ubuntu@35.90.160.204:~/'
              sh 'ssh ubuntu@35.90.160.204 "chmod +x deploy.sh"'
              sh 'ssh ubuntu@35.90.160.204 ./deploy.ssh'
    }
}