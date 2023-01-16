pipeline {
    agent {
        node {
            label 'ubuntu-agent'
        }
    }
    stages {
        stage('checkstyle') {
            steps {
                sh './gradlew checkstyleMain'
                archiveArtifacts artifacts: 'build/reports/checkstyle/main.html'
            }
        }
        stage('test') {
            steps {
                sh './gradlew test'
            }
        }
        stage('build') {
            steps {
                sh './gradlew build -x test'
            }
        }
    }
}