pipeline {
    agent {
        node {
            label 'ubuntu-master'
        }
    }
    stages {
        stage('Build') {
            steps {
                sh './gradlew build'
                // archiveArtifacts artifacts: './build/libs/reports/checkstyle/main.html'
            }
        }
    }
}