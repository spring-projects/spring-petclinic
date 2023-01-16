pipeline {
    agent {
        node {
            label 'linux-agent'
        }
    }
    stage('Build') {
        steps {
            // echo 'java -version' 
            withGradle(){
            sh './gradlew -version' 
            sh './gradlew clean test --no-daemon'
            }
            // sh 'cat ./build/libs/spring-petclinic-changed-0.1.1-SNAPSHOT-plain.jar'
            // archiveArtifacts: './build/libs/spring-petclinic-changed-0.1.1-SNAPSHOT-plain.jar'
            // withGradle(){
            // }
        }
    }
}