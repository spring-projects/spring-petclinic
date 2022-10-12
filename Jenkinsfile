node('JDK-11-MVN3.8.6'){
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
        sh 'docker image build --tag dockerimage'
        sh 'docker image ls'
    }
}