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
                sh 'pwd'
                archiveArtifacts artifacts: '/var/lib/jenkins/workspace/pipeline/build/libs/reports/checkstyle/main.html', fingerprint: true
            }
        }
    }
}