pipeline {
    agent {
        node {
            label 'linux-agent'
        }
    }
    stages {
        stage('Build') {
            steps {
                sh './gradlew build --no-daemon'
                archiveArtifacts artifacts: './build/libs/reports/checkstyle/main.html'
            }
        }
    }
}