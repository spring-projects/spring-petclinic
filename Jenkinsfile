pipeline {
    agent any

    tools {
        gradle '8.7'
    }

    stages {
        stage('Checkstyle') {
            steps{
                echo 'Running gradle checkstyle'
                sh './gradlew checkstyleMain --no-daemon'
            }
        }
        stage('Test') {
            steps {
                echo 'Running gradle test'
                sh './gradlew test --no-daemon'
            }
        }
        stage('Build') {
            steps {
                echo 'Running build automation'
                sh './gradlew clean build -x test -x check -x checkFormat -x processTestAot --no-daemon'
                archiveArtifacts artifacts: 'build/libs/*.jar'
            }
        }
    }
}