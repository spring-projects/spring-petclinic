node('JDK-11-MVN3.8.6'){
    stage('source code '){
        git branch: 'scripted', url: 'https://github.com/ShaikNasee/spring-petclinic.git'
    }
    stage('mvn build'){
        sh '/usr/local/apache-maven-3.8.6/bin/mvn clean packaage'

    }
    stage('archive Artifacts'){
        rchiveArtifacts artifacts: 'target/*.jar', followSymlinks: false
    }
    stage('pushing unit test reports'){
        junit '**TEST*-.xml'
    }
    stage('docker image build '){
        sh 'docker image build --tage dockerimage:1.0'
        sh 'docker image ls'
    }
}