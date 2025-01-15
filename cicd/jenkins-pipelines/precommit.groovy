pipeline {
    agent {
        label 'agent1'
    }
    triggers {
        pollSCM('H/5 * * * *') // Adjust polling interval as needed
    }
    stages {
        stage('Checkout Code') {
            steps {
                git url: 'https://github.com/Nalexx06/spring-petclinic.git', branch: 'dev'
            }
        }
        stage('Build & Test') {
            steps {
                sh './mvnw clean verify' // Uses Maven Wrapper to build and test
            }
        }
    }
}
