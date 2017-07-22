pipeline {
    agent { docker 'maven:3.3.3' }
    stages {
        stage('pre-build') {
            steps {
                sh 'mvn --version'
            }
        }
        stage('build') {
            steps {
                sh 'mvn compile'
            }
        }
    }
}
