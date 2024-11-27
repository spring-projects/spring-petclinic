pipeline {
    agent {
        label 'docker'
    }

    stages {
        stage('Checkstyle') {
            steps {
                sh 'mvn checkstyle:checkstyle'
            }
        }
    }
}
