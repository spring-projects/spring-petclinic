pipeline {
    agent any
    triggers {
        pollSCM('H/5 * * * *') // Adjust polling interval as needed
    }
    stages {
        stage('Checkout Code') {
            steps {
                git url: 'https://github.com/spring-projects/spring-petclinic.git', branch: 'main'
            }
        }
        stage('Build & Test') {
            steps {
                sh './mvnw clean verify' // Uses Maven Wrapper to build and test
            }
        }
    }
}
