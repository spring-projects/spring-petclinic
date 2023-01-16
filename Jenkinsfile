pipeline {
    agent {
        node {
            label 'ubuntu-master'
        }
    }
    stages {
        stage('Build') {
            steps {
                sh 'chmod +x gradlew'
                sh './gradlew tasks'
                sh './gradlew checkstyleMain'
            
                sh 'ls -a'
                
                archiveArtifacts artifacts: 'build/reports/checkstyle/main.html'
            }
        }
    }
}