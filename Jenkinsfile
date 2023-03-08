pipeline {
    agent { label 'Master'}
    triggers { pollSCM('* * * * *') }
    stages{
        stage('vcs') {
            steps {
                git branch: 'master',
                    url: 'https://github.com/Bharatkumar5690/spring-petclinic.git'
            }
        }
        stage('Build') {
            steps {
                sh 'mvn package'
            }
        }
    }
}
