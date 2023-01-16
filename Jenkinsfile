pipeline {
    agent {
        node {
            label 'ubuntu-master'
        }
    }
    stages {
        stage('Build') {
            steps {
                sh './gradlew -version'
                // archiveArtifacts artifacts: './build/libs/reports/checkstyle/main.html'
            }
        }
    }
}