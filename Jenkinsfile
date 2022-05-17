pipeline {
    agent any
    triggers{
        // Triggers pipeline for every commit on git repo
        pollSCM '* * * * *'
    }
   
    stages {
        agent {
            label 'JDK11'
        }
        stage('Build 1') {
            steps {
                // Get some code from a GitHub repository
                git branch: 'main', credentialsId: 'nrajulearning', url: 'https://github.com/nrajulearning/spring-petclinic.git'

                // Run Maven on a Unix agent.
                sh "mvn clean package"

                // get the host information
                sh '$HOSTNAME'
                sh 'cat /etc/*-release'

                            }
        }
        agent {
            label 'JDK1.8'
        }
        stage('Build 2'){
            steps{
                // get the code from github
                git branch: 'main',credentialsId: 'nrajulearning', url: 'https://github.com/nrajulearning/spring-petclinic.git'

                // Run the Maven build
                sh "mvn clean package"
                // get the host information
                sh '$HOSTNAME'
                sh 'cat /etc/*-release'
            }
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
