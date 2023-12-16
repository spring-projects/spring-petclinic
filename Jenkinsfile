pipeline {
    agent any
    options { 
        timeout(time: 1, unit: 'HOURS')
        retry(2)
    }
    stages {
        stage ('SourceCode') {
            steps {
                //source code from github
                git branch: 'main', url: 'https://github.com/vishnu1411/spring-petclinic.git'
            }

        }
        stage ('Build and install') {
            steps {
                //clean and build using maven
                sh 'mvn clean install'
            }

        }
        stage('Build a Docker Image') {
            steps {
                // Build a Docker image
                script {
                    docker.build('petclinic:auto')
                }
            }
        }
        stage ('Archive test results') {
            steps {
                //Archive the test results
                junit '**/surefire-reports/*xml'
                archiveArtifacts artifacts: '**/*.jar', followSymlinks: false
            }

        }
    }
}