pipeline {
    agent any
    environment {
        IMAGE_NAME = "your-dockerhub-username/spring-petclinic:${env.BUILD_NUMBER}"
    }
    stages{
        stage('checkout')
        {
            steps{
                echo "Clonning Reposotiory"
                checkout scm
            }
        }
        stage('Cleaning stage'){
            steps{
                echo "Builing jar file without testing"
                sh'./mvnw clean'
            }
        }
        stage('build stage'){
            steps{
                echo "Builing jar file without testing"
                sh'./mvnw package -DskipTests'
            }
        }
        stage('test stage'){
            steps{
                echo "Running Unit tests"
                withSonarQubeEnv('SonarQube-server') {
                    sh'./mvnw sonar:sonar'
                }
            }
        }
        stage('Build Docker Image') {
            steps {
                script {           
                    echo "Building Docker image: ${IMAGE_NAME}"
                    sh "docker build -t ${IMAGE_NAME} ."
                }
            }
        }
        stage('Push Docker Image') {
            steps {
                script {
                    echo "Pushing Docker image: ${IMAGE_NAME}"
                    withCredentials([usernamePassword(credentialsId: 'docker-login', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                        sh "echo ${DOCKER_PASS} | docker login -u ${DOCKER_USER} --password-stdin"
                    }
                    sh "docker push ${IMAGE_NAME}"
                }
            }
        }
    }
}
// test1
// test2
// test3