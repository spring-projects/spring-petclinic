pipeline {
    agent any 

    triggers {
        cron('H/10 * * * 1') // This triggers the job every 10 minutes on Mondays
    }

    stages {
        stage('Build') {
            steps {
                script {
                    // Ensure you have Maven installed in your Jenkins instance
                    sh 'mvn clean package'
                }
            }
        }

        stage('Code Coverage') {
            steps {
                script {
                    // Run tests and generate code coverage report using Jacoco
                    sh 'mvn clean test jacoco:report'
                }
            }
        }
    }

    post {
        always {
            // Archive the code coverage report
            junit '**/target/surefire-reports/*.xml' // Adjust the path if necessary
            publishCoverage adapters: [jacocoAdapter('**/target/jacoco.exec')]
        }
    }
}
