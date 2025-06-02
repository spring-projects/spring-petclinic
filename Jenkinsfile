pipeline {
    agent agent1
    stages {
        stage ('Build') {
            steps {
                sh 'mvn checkstyle:check'
            }
        }
    }
}