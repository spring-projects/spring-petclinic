pipeline {
    agent { label 'pipeline-agent' } 
    stages {
        stage ('Build') {
            steps {
                sh 'mvn checkstyle:check'
            }
        }
    }
}