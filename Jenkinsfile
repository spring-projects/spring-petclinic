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
                    // Grab branch name
                    env.GIT_BRANCH_NAME = sh(script: "git rev-parse --abbrev-ref HEAD", returnStdout: true).trim()
                    // Grab short commit
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
                // Archive checkstyle reports as artifacts
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
                    // Decide which repo to push to based on the branch
                    if (env.GIT_BRANCH_NAME == 'main') {
                        // main branch
                        echo "Building Docker image for MAIN repo..."
                        sh "docker build -t ${DOCKERHUB_USERNAME}/main:${GIT_COMMIT_SHORT} ."

                        // Login & push
                        withCredentials([usernamePassword(
                            credentialsId: "${DOCKERHUB_CREDENTIALS}",
                            usernameVariable: 'DOCKER_USER',
                            passwordVariable: 'DOCKER_PASS'
                        )]) {
                            sh "echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin"
                        }
                        
                        sh "docker push ${DOCKERHUB_USERNAME}/main:${GIT_COMMIT_SHORT}"
                        // Optional: push latest tag !!!
                        sh "docker tag ${DOCKERHUB_USERNAME}/main:${GIT_COMMIT_SHORT} ${DOCKERHUB_USERNAME}/main:latest"
                        sh "docker push ${DOCKERHUB_USERNAME}/main:latest"
                        
                    } else {
                        // merge-request or feature branch
                        echo "Building Docker image for MR repo..."
                        sh "docker build -t ${DOCKERHUB_USERNAME}/mr:${GIT_COMMIT_SHORT} ."

                        withCredentials([usernamePassword(
                            credentialsId: "${DOCKERHUB_CREDENTIALS}",
                            usernameVariable: 'DOCKER_USER',
                            passwordVariable: 'DOCKER_PASS'
                        )]) {
                            sh "echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin"
                        }

                        sh "docker push ${DOCKERHUB_USERNAME}/mr:${GIT_COMMIT_SHORT}"
                        // Optional: push latest tag
                        sh "docker tag ${DOCKERHUB_USERNAME}/mr:${GIT_COMMIT_SHORT} ${DOCKERHUB_USERNAME}/mr:latest"
                        sh "docker push ${DOCKERHUB_USERNAME}/mr:latest"
                    }
                }
            }
        }
    }
}
