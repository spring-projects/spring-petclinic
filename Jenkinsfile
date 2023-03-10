pipeline {
    agent { label 'Master'}
    tools {
            jdk 'JDK_17'
            maven 'MAVEN'
        }
    stages{
        stage('vcs') {
            steps {
                git branch: 'declarative',
                    url: 'https://github.com/Bharatkumar5690/spring-petclinic.git'
            }
        }
        stage('package') {
            steps {
                sh 'mvn package'
            }
        }
        stage('Test the code by using sonarqube') {
            steps {
                withSonarQubeEnv('SONAR_CLOUD') {
                    sh 'mvn clean verify sonar:sonar -Dsonar.login=ea06c1ce5d1ee81e35db29d8cb0de69b42c70278 -Dsonar.organization=springpetclinic-1 -Dsonar.projectKey=springpetclinic-1_bha'
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
