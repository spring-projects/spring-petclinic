pipeline {
    agent { label 'SPC'}
    stages {
        stage(‘Build’) {
            steps {
                sh 'mvn --version'
                sh 'mvn package'
            }
        }
    }
}