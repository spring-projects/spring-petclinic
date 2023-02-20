pipeline {
    agent any
    tools{
        maven '3.9.0'
    }
    stages{
        stage('Get Code and Build App'){
            steps{
                checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/mansiboriya/spring-petclinic']])
                sh 'docker build -t springpetclinic:test -f Dockerfile.test .'
                sh 'docker run springpetclinic:test'
            }
        }
        stage('Build docker image'){
            steps{
                script{
                    sh 'docker build -t springpetclinic:latest .'
                }
            }
        }
    }
}