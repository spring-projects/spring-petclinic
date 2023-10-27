pipeline {

    agent none

    stages {

        stage('Build') {
            agent {
                docker { image 'maven:3.6.3-openjdk-11-slim' }
            }
            
            steps {
                sh 'mvn package -Dmaven.test.skip'
            }

            post{
                success {
                    archiveArtifacts artifacts: 'target/*.jar', fingerprint: true, onlyIfSuccessful: true
                }
            }
        }
    }
}
