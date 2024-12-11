pipeline {
    agent any
    stages {
        stage ('Checkstyle') {
            steps {
                sh 'mvn validate'
                archiveArtifacts artifacts: 'target/checkstyle-report.xml', allowEmptyArchive: true
            }
        }
    }
}