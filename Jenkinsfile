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
                echo 'Hello hgfds world!' 
                sh './gradlew build --no-daemon'
                archiveArtifacts: 'build/libs/spring-petclinic-changed-0.1.1-SNAPSHOT-plain.jar'
                // withGradle(){
                // }
            }
        }
    }
}