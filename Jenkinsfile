#!/usr/bin/env groovy

pipeline {
    agent any

    environment {
        DOCKERHUB_CREDENTIALS = 'dockerhub-credentials'
        DOCKERHUB_USERNAME = 'marijastopa'
    }

    stages {
        stage('Prepare') {
            steps {
                script {
                    env.GIT_BRANCH_NAME = sh(script: "git rev-parse --abbrev-ref HEAD", returnStdout: true).trim()
                    env.GIT_COMMIT_SHORT = sh(script: "git rev-parse --short HEAD", returnStdout: true).trim()
                    echo "Branch: ${env.GIT_BRANCH_NAME}"
                    echo "Commit: ${env.GIT_COMMIT_SHORT}"
                }
            }
        }
        
        stage('Checkstyle') {
            when {
                expression { return env.GIT_BRANCH_NAME != 'main' }
            }
            steps {
                echo 'Running Checkstyle...'
                sh "./gradlew checkstyleMain checkstyleTest"
                archiveArtifacts artifacts: 'build/reports/checkstyle/*.xml', fingerprint: true
            }
        }
        
        stage('Test') {
            when {
                expression { return env.GIT_BRANCH_NAME != 'main' }
            }
            steps {
                echo 'Running tests...'
                sh "./gradlew test"
            }
        }
        
        stage('Build without Tests') {
            when {
                expression { return env.GIT_BRANCH_NAME != 'main' }
            }
            steps {
                echo 'Building application (excluding tests)...'
                sh "./gradlew clean build -x test"
            }
        }
        
        stage('Docker Build & Push') {
            steps {
                script {
                    def dockerRepo = (env.GIT_BRANCH_NAME == 'main') ? 'main-jenkins' : 'mr-jenkins'
                    
                    echo "Building Docker image for ${dockerRepo} repo..."
                    sh "docker build -t ${DOCKERHUB_USERNAME}/${dockerRepo}:${GIT_COMMIT_SHORT} ."
                    
                    withCredentials([usernamePassword(
                        credentialsId: "${DOCKERHUB_CREDENTIALS}",
                        usernameVariable: 'DOCKER_USER',
                        passwordVariable: 'DOCKER_PASS'
                    )]) {
                        sh "echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin"
                    }
                    
                    sh "docker push ${DOCKERHUB_USERNAME}/${dockerRepo}:${GIT_COMMIT_SHORT}"
                    sh "docker tag ${DOCKERHUB_USERNAME}/${dockerRepo}:${GIT_COMMIT_SHORT} ${DOCKERHUB_USERNAME}/${dockerRepo}:latest"
                    sh "docker push ${DOCKERHUB_USERNAME}/${dockerRepo}:latest"
                }
            }
        }
    }
}
