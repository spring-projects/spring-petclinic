pipeline {
    agent {
        docker {
            image 'maven:3-alpine'
        }
    }
    stages {
        stage ('Build') {
            steps {
                git branch: 'master', url: 'https://github.com/liatrio/spring-petclinic.git'
                sh 'mvn deploy'
            }
        }
        stage ('Sonar Analysis') {
            steps {
                script {
                    scannerHome = tool 'Sonar'
                }

                withSonarQubeEnv('Sonar') {
                    sh "${scannerHome}/bin/sonar-scanner"
                }
            }
        }
    }
}
