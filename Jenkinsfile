pipeline {
    agent {label 'node'}
    stages {
        stage ('git clone') {
            steps {
                git branch: 'main', 
                    url: 'https://github.com/gopivurata/spring-petclinic.git'
            }
        }
        stage ('docker image build') {
            steps {
                sh 'docker image build -t spring-petclinic:1.0 .'
            }
        }
    }
}