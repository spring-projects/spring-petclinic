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
                sh 'ls -a'
                sh 'cd ./build/reports/checkstyle'
                sh 'ls -a'
                
                archiveArtifacts artifacts: 'build/reports/checkstyle/main.html'
            }
        }
    }
}