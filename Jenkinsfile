pipeline {
    agent any
    environment {
        DOCKER_IMAGE = "prathushadevijs/spring-petclinic-proj"
    }
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Create Git Tag') {
            steps {
                script {
                    // Make sure the script has execute permissions
                    sh 'chmod +x ./increment_version.sh'
                    // Run the version increment script
                    sh './increment_version.sh'
                }
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
        stage('Get Git Tag') {
            steps {
                script {
                    // Fetch the most recent Git tag
                    env.gitTag = sh(script: 'git describe --tags --abbrev=0', returnStdout: true).trim()
                    echo "Using Git Tag: ${gitTag}"
                }
            }
        }
        stage('Build Docker Image') {
            steps {
                script {
                    // Use the gitTag variable set earlier
                    sh "docker build -t ${DOCKER_IMAGE}:${gitTag} ."
                }
            }
        }
        stage('Push to Docker Hub') {
            steps {
                script {
                    // Push the Docker image with the gitTag
                    withCredentials([usernamePassword(credentialsId: 'dockerhub-creds', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                        sh "docker push ${DOCKER_IMAGE}:${gitTag}"
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
                            ssh-keyscan -H 34.201.2.231 >> ~/.ssh/known_hosts
                            ssh -i $KEYFILE ec2-user@34.201.2.231 'sudo docker pull ${DOCKER_IMAGE}:${gitTag} && \
                            sudo docker stop spring-petclinic || true && \
                            sudo docker rm spring-petclinic || true && \
                            sudo docker run -d --name spring-petclinic \
                              -e DB_HOST=terraform-20250201163508870100000001.cx8weoi6ueor.us-east-1.rds.amazonaws.com \
                              -e DB_PORT=3306 \
                              -e DB_NAME=springpetclinicdb \
                              -e DB_USERNAME=admin \
                              -e DB_PASSWORD=projRDS123 \
                              -p 8081:8080 \
                              ${DOCKER_IMAGE}:${gitTag}   
                        """
                    }
                }
            }
        }
    }
}
