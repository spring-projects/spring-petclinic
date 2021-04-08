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
                     docker build -t rodley/pet-clinic:${BUILD_NUMBER} -f Dockerfile
                   '''
            }
        }
        stage('Push artifact to docker registry') {
            steps {
                echo 'Push docker image tu registry'
                withDockerRegistry(credentialsId: 'dockerhub_id') {
                sh "docker push rodley/pet-clinic:${BUILD_NUMBER}"
                }
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying....'
            }
        }
    }
}
