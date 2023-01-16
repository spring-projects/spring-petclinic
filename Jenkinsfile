pipeline {
    agent {
        node {
            label 'linux-agent'
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