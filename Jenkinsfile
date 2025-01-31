pipeline {
    agent any
    environment {
        DOCKER_IMAGE = "prathushadevijs/spring-petclinic-proj"
        DOCKER_TAG = "${GIT_COMMIT}"
    }
    stages {
        stage('Checkout') {
            steps {
                checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[credentialsId: 'github-creds', url: 'https://github.com/Prathushadevijsgd/spring-petclinic']])
            }
        }
        stage('Build & Test') {
            steps {
                script {
                    sh 'mvn clean install -DskipTests'
                }
            }
        }
        stage('Login to Docker Hub') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'dockerhub-creds', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                        sh 'docker login -u $DOCKER_USERNAME -p $DOCKER_PASSWORD'
                    }
                }
            }
        }
        stage('Build Docker Image') {
            steps {
                script {
                    sh 'docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} .'
                }
            }
        }
        stage('Push to Docker Hub') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'dockerhub-creds', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                        sh 'docker push ${DOCKER_IMAGE}:${DOCKER_TAG}'
                    }
                }
            }
        }
        stage('Deploy to EC2') {
            steps {
                script {
                    withCredentials([sshUserPrivateKey(credentialsId: 'ec2-ssh-key', keyFileVariable: 'KEYFILE')]) {
                        sh """
                            mkdir -p ~/.ssh
                            ssh-keyscan -H 34.232.44.227 >> ~/.ssh/known_hosts
                            ssh -i $KEYFILE ec2-user@34.232.44.227 'docker pull ${DOCKER_IMAGE}:${DOCKER_TAG} && \
                            docker stop spring-petclinic || true && \
                            docker rm spring-petclinic || true && \
                            docker run -d --name spring-petclinic -p 8081:8080 ${DOCKER_IMAGE}:${DOCKER_TAG}'
                        """
                    }
                }
            }
        }
    }
}
