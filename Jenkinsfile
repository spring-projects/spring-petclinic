pipeline {
    agent any
    options {
        buildDiscarder(logRotator(numToKeepStr: '10'))
        timestamps()
        retry(3)
    }
    stages {
        stage('maven build') {
            steps {
                sh 'mvn clean package'
            }
        }
        stage('allure reports') {
            steps {
                allure includeProperties: false, jdk: '', results: [[path: 'target/allure-results']]
            }
        }
    }
}
