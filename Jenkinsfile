pipeline {
    agent any
     options { buildDiscarder(logRotator(numToKeepStr: '10')) }
     options { timestamps() }

    stages {
        stage('frontend') {
            steps {
                sh 'cd frontend'
            }
        }
        stage('maven build') {
            steps {
                sh 'clean package'
            }
        }
        stage('allure reports') {
            steps {
                allure includeProperties: false, jdk: '', results: [[path: 'target/allure-results']]
            }
        }
    }
}
