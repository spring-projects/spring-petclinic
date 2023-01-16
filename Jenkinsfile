pipeline {
    agent {
        node {
            label 'linux-agent'
        }
    }
    stages {
        stage('Build') {
            steps {
                sh 'java -version' 
                withGradle(){
                sh './gradlew -version' 
                sh './gradlew clean build --no-daemon'
                }
                archiveArtifacts artifacts: './build/libs/reports/checkstyle/main.html'
            }
        }
    }
}