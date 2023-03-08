pipeline {
    agent { label 'Master'}
    stages{
        stage('vcs') {
            steps {
                git branch: 'declarative',
                    url: 'https://github.com/Bharatkumar5690/spring-petclinic.git'
            }
        }
        stage('Build') {
            steps {
                sh 'mvn package'
            }
        }
        stage('Test the code by using sonarqube') {
            steps {
                withSonarQubeEnv('SONAR_CLOUD') {
                    sh 'mvn clean package sonar:sonar'
                }
            }
        }
        stage('Gathering the artifacts & test results') {
            steps {
                archiveArtifacts artifacts: '**/target/*.jar',
                                    onlyIfSuccessful: true,
                                    allowEmptyArchive: true
                junit testResults: '**/surefire-reports/TEST-*.xml'
            }
        }
    }
}
