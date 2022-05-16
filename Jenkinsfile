pipeline {
    agent { label 'JDK11' }

    tools {
        // Install the Maven version configured as M2
        maven "Default"
    }

    stages {
        stage('Build') {
            steps {
                // Get some code from a GitHub repository
                git branch: 'main', credentialsId: 'nrajulearning', url: 'https://github.com/nrajulearning/spring-petclinic.git'

                // Run Maven on a Unix agent.
                sh "mvn clean package"

                            }

            post {
                // If Maven was able to run the tests, even if some of the test
                // failed, record the test results and archive the jar file.
                success {
                    junit '**/target/surefire-reports/TEST-*.xml'
                    archiveArtifacts 'target/*.jar'
                }
            }
        }
    }
}
