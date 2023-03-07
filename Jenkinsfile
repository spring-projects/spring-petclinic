pipeline {
    agent { label 'AAA' }
    triggers { pollSCM ('* 21 * * 1-5') }
    stages {
        stage('vcs') {
            steps {
                git url: 'https://github.com/arjun9963/spring-petclinic.git',
                    branch: 'dev'
            }
        }
        stage('package') {
            steps {
                sh 'mvn package'
            }
        }
        stage('post build') {
            steps {
                archiveArtifacts artifacts: '**/target/spring-petclinic.war',
                                 onlyIfSuccessful: true
                junit testResults: '**/surefire-reports/TEST-*.xml'
            }
        }
    }
}