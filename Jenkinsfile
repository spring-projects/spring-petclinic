pipeline {
    agent any

    stages {
        stage('Checkout SCM') {
            steps {
                checkout scm
            }
        }
        stage ('Test maven') {
                steps {
                sh '''
                    mvn --version
                    mvn clean test surefire-report:report
                '''
                }
        }
        stage('Build') {
            steps {
                echo 'Running build automation'
                sh '''
                        ./mvnw package
                   '''
           }
        }
        stage('CREATE ARTIFACT') {
            steps {
                archiveArtifacts artifacts: 'spring-petclinic-2.4.2.jar', fingerprint: true, followSymlinks: false, onlyIfSuccessful: true
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying....'
            }
        }
    }
}
