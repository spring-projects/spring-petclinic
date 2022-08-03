pipeline {
    agent any
    stages {
        stage('Checkout external proj') {
            steps {
                git branch: 'feature-newimg',
                    credentialsId: 'GitHub_SSH_Key_jenkins',
                    url: 'git@github.com:YuriyPelykh/spring-petclinic.git'
                sh "ls -lat"
            }
        }
    }
}




