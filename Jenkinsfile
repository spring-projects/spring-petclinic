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

                    // Add this line for branch name confirmation
                    echo "Detected Branch: ${env.GIT_BRANCH_NAME}"
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
                    if (env.GIT_BRANCH_NAME == 'main') {
                        echo "Building Docker image for main-jenkins repo..."
                        sh "docker build -t ${DOCKERHUB_USERNAME}/main-jenkins:${GIT_COMMIT_SHORT} ."

                        withCredentials([usernamePassword(
                            credentialsId: "${DOCKERHUB_CREDENTIALS}",
                            usernameVariable: 'DOCKER_USER',
                            passwordVariable: 'DOCKER_PASS'
                        )]) {
                            sh "echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin"
                        }

                        sh "docker push ${DOCKERHUB_USERNAME}/main-jenkins:${GIT_COMMIT_SHORT}"
                        sh "docker tag ${DOCKERHUB_USERNAME}/main-jenkins:${GIT_COMMIT_SHORT} ${DOCKERHUB_USERNAME}/main-jenkins:latest"
                        sh "docker push ${DOCKERHUB_USERNAME}/main-jenkins:latest"
                    } else {
                        echo "Building Docker image for mr-jenkins repo..."
                        sh "docker build -t ${DOCKERHUB_USERNAME}/mr-jenkins:${GIT_COMMIT_SHORT} ."

                        withCredentials([usernamePassword(
                            credentialsId: "${DOCKERHUB_CREDENTIALS}",
                            usernameVariable: 'DOCKER_USER',
                            passwordVariable: 'DOCKER_PASS'
                        )]) {
                            sh "echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin"
                        }

                        sh "docker push ${DOCKERHUB_USERNAME}/mr-jenkins:${GIT_COMMIT_SHORT}"
                        sh "docker tag ${DOCKERHUB_USERNAME}/mr-jenkins:${GIT_COMMIT_SHORT} ${DOCKERHUB_USERNAME}/mr-jenkins:latest"
                        sh "docker push ${DOCKERHUB_USERNAME}/mr-jenkins:latest"
                    }
                }
            }
        }
    }
}
