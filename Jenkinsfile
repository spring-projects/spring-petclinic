pipeline {
    agent { docker 'maven:3.3.3' }
    stages {
        stage('prebuilt') {
            steps {
                sh 'whoami'
            }
        }
        stage('build') {
            steps {
                sh 'mvn --version'
            }
        }
    }
}
