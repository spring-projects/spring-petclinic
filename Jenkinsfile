pipeline {
    agent {
        node {
            label 'linux-agent'
        }
    }
    stages {
        stage('Clone') {
            steps {
                echo 'cfscfs cfscfs csfc!' 
                sh 'ls -a' 
                sh "git clone https://github.com/RolandBakunts/spring-petclinic.git"
                sh "ls -a"
                sh "./gradlew clean build"
                // withGradle(){
                // }
            }
        }
        stage('Build') {
            steps {
                echo 'Hello hgfds world!' 
                sh './gradlew task --all'
                // withGradle(){
                // }
            }
        }
    }
}