pipeline {
    agent any
    environment {
        IMAGE_NAME = "karimfathy1/petclinic-app:${env.BUILD_NUMBER}"
    }
    stages{
        stage('Checking environment varaibles'){
            steps{
                echo "BRANCH_NAME  = ${env.BRANCH_NAME}"
                echo "IMAGE_NAME   = ${IMAGE_NAME}"
                echo "BUILD_NUMBER = ${env.BUILD_NUMBER}"
            }
        }
        stage('checkout'){
            steps{
                echo "Clonning Reposotiory"
                checkout scm
            }
        }
        stage('Cleaning stage'){
            steps{
                echo "Cleaning Project"
                sh './mvnw clean'
            }
        }
        stage('build stage'){
            steps{
                echo "Building jar file without testing"
                sh'./mvnw package -DskipTests'
            }
        }
        stage('test stage'){
            steps{
                echo "Running Unit tests"
               // sh './mvnw test'
                withSonarQubeEnv('SonarQube-server'){
                    withCredentials([string(credentialsId: 'sonar-token', variable: 'SONAR_TOKEN')]) {
                        sh './mvnw sonar:sonar -Dsonar.login=$SONAR_TOKEN'
                    }
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