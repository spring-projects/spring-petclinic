pipeline {
    agent any
    tools {
        maven 'Maven'
    }
    stages {
        stage ('Build') {
            steps {
                git branch: 'plumber_dmitry', url: 'https://github.com/liatrio/spring-petclinic.git'
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
