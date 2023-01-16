pipeline {
    agent {
        node {
            label 'ubuntu-master'
        }
    }
    stages {
        stage('Build') {
            steps {
                // sh 'chmod +x gradlew'
                // sh './gradlew build'
                archiveArtifacts artifacts: 'build/libs/reports/checkstyle/main.html', fingerprint: true
            }
        }
    }
}