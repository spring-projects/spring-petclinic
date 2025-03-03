pipeline {
    agent any

    triggers {
        cron('H 3 * * 1')  // Triggers every Monday at 3 minutes past any hour.
    }

    environment {
       
    }

    stages {
        stage('Build') {
            steps {
                script {
                    // Run the Maven build to generate artifact (e.g., JAR file)
                    sh 'mvn clean install'
                }
            }
        }

        stage('Jacoco Code Coverage') {
            steps {
                script {
                    // Run Jacoco to generate code coverage report
                    sh 'mvn jacoco:prepare-agent test jacoco:report'
                }
            }
        }

        stage('Publish Code Coverage') {
            steps {
                script {
                    // Archive Jacoco report to Jenkins
                    publishHTML(target: [
                        reportName: 'Jacoco Report',
                        reportDir: 'target/site/jacoco',
                        reportFiles: 'index.html',
                        keepAll: true
                    ])
                }
            }
        }
    }
}
