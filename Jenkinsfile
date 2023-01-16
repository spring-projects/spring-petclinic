pipeline {
    agent {
        node {
            label 'linux-agent'
        }
    }
    stages {
        // stage('Clone') {
        //     steps {
        //         echo 'cfscfs cfscfs csfc!' 
        //         sh './gradlew clean build' 
        //         git "git clone https://github.com/RolandBakunts/spring-petclinic.git"
        //         sh "ls -a"
        //         sh "./gradlew clean build"
        //         // withGradle(){
        //         // }
        //     }
        // }
        stage('Build') {
            steps {
                // echo 'java -version' 
                withGradle(){
                sh './gradlew -version' 
                }
                // sh 'cat ./build/libs/spring-petclinic-changed-0.1.1-SNAPSHOT-plain.jar'
                // archiveArtifacts: './build/libs/spring-petclinic-changed-0.1.1-SNAPSHOT-plain.jar'
                // withGradle(){
                // }
            }
        }
    }
}