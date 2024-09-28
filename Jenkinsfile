// Jenkins Pipeline Declarative CICD Flow

pipeline {
    agent any
    stages {

        stage("build") {
            steps {
                sh "./mvnw package"
            }
        }
        
        stage("artifact") {
            steps {
                archiveArtifacts '**/target/*.jar'
                jacoco()
                junit '**/target/surefire-reports/TEST*.xml'
            }
        }
    }

    // post actions
    post {
        always {
            emailext body: 'Test Message',
                recipientProviders: [developers(), requestor()],
                subject: 'Test Subject',
                to: 'test@example.com'
        }
    }
}