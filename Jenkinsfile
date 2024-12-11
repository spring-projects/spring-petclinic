pipeline {
    agent any
    stages {
        stage ('Checkstyle') {
            steps {
                sh 'mvn checkstyle:checkstyle'
                archiveArtifacts artifacts: 'target/checkstyle-report.xml', allowEmptyArchive: true
            }
        }
        stage ('Test') {
            steps {
                sh 'echo "Starting tests..."'
                sh 'mvn test'
                junit 'target/surefire-reports/*.xml'
            }
        }
        stage ('Build') {
            steps {
                sh 'echo "Building..."'
                sh 'mvn clean package'
                archiveArtifacts artifacts: 'target/*.jar', allowEmptyArchive: false
            }
        }
    }
}