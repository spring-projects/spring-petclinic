pipeline {
    agent any 
    triggers {
        cron('H/10 * * * 1') // Trigger every 10 minutes on Mondays
    }
    stages {
        stage('Build') {
            steps {
                script {
                    // Run Maven build
                    sh 'mvn clean package'
                }
            }
        }
        stage('Code Coverage') {
            steps {
                script {
                    // Run tests and generate Jacoco report
                    sh 'mvn test jacoco:report'
                }
            }
        }
    }
    post {
        always {
            // Archive the Jacoco report
            archiveArtifacts artifacts: 'target/site/jacoco/*.html', fingerprint: true
        }
    }
}
