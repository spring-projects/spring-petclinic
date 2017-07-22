pipeline {
    agent { docker 'maven:3.3.3' }
    stages {
        stage('prebuild') {
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
