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
                echo 'Creating Docker Image...'
                sh '''
                     docker build -t rodley/pet-clinic:${BUILD_NUMBER} -f Dockerdile
                   '''
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying....'
            }
        }
    }
}
