pipeline {
    agent any
    environment {
        DOCKER_IMAGE_MAIN = 'marijastopa/main-jenkins'
        DOCKER_IMAGE_MR = 'marijastopa/mr-jenkins'
        GRADLE_BUILD_IMAGE = "gradle:8.12.0-jdk17"
        DOCKER_BUILD_IMAGE = "docker:27"
        DOCKER_CREDENTIALS_ID = 'dockerhub-credentials'  // Jenkins credential ID for Docker Hub
    }
    stages {
        stage('Prepare') {
            steps {
                echo "Preparing environment..."
                script {
                    BRANCH_NAME = env.BRANCH_NAME ?: 'main'
                    COMMIT = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
                    echo "Branch: ${BRANCH_NAME}"
                    echo "Commit: ${COMMIT}"
                }
            }
        }
        stage('Checkstyle') {
            when {
                expression {
                    BRANCH_NAME != 'main'
                }
                expression { env.CHANGE_ID != null }  // Only run for merge requests
            }
            agent {
                docker { image "${GRADLE_BUILD_IMAGE}" }
            }
            steps {
                echo "Running Checkstyle..."
                sh './gradlew checkstyleMain checkstyleTest'
                archiveArtifacts artifacts: '**/build/reports/checkstyle/*.xml', allowEmptyArchive: true
                sh 'gradle checkstyleMain'
                archiveArtifacts artifacts: 'build/reports/checkstyle/**/*', allowEmptyArchive: true
            }
        }
        stage('Test') {
            when {
                expression {
                    BRANCH_NAME != 'main'
                }
                expression { env.CHANGE_ID != null }  // Only run for merge requests
            }
            agent {
                docker { image "${GRADLE_BUILD_IMAGE}" }
            }
            steps {
                echo "Running tests..."
                sh './gradlew test'
                sh 'gradle test'
            }
        }
        stage('Build without Tests') {
            when {
                expression {
                    BRANCH_NAME != 'main'
                }
                expression { env.CHANGE_ID != null }  // Only run for merge requests
            }
            agent {
                docker { image "${GRADLE_BUILD_IMAGE}" }
            }
            steps {
                echo "Building application (without tests)..."
                sh './gradlew clean build -x test'
                sh 'gradle clean build -x test'
            }
        }
        stage('Docker Build & Push (Merge Request)') {
            when {
                expression {
                    BRANCH_NAME != 'main'
                }
                expression { env.CHANGE_ID != null }  // Only run for merge requests
            }
            agent {
                docker { image "${DOCKER_BUILD_IMAGE}" }
            }
            steps {
                echo "Building Docker image for merge request..."
                withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh """
                        docker build -t $DOCKER_IMAGE_MR:${COMMIT} .
                        echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                        docker push $DOCKER_IMAGE_MR:${COMMIT}
                        docker tag $DOCKER_IMAGE_MR:${COMMIT} $DOCKER_IMAGE_MR:latest
                        docker push $DOCKER_IMAGE_MR:latest
                    """
                withCredentials([usernamePassword(credentialsId: "${DOCKER_CREDENTIALS_ID}", usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh '''
                        echo "$DOCKER_PASS" | docker login --username "$DOCKER_USER" --password-stdin
                        docker build -t marijastopa/mr-jenkins:${GIT_COMMIT:0:7} .
                        docker push marijastopa/mr-jenkins:${GIT_COMMIT:0:7}
                    '''
                }
            }
        }
        stage('Docker Build & Push (Main Branch)') {
            when {
                branch 'main'
                branch 'main'  // Only run for the main branch
            }
            agent {
                docker { image "${DOCKER_BUILD_IMAGE}" }
            }
            steps {
                echo "Building Docker image for main branch..."
                withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh """
                        docker build -t $DOCKER_IMAGE_MAIN:${COMMIT} .
                        echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                        docker push $DOCKER_IMAGE_MAIN:${COMMIT}
                        docker tag $DOCKER_IMAGE_MAIN:${COMMIT} $DOCKER_IMAGE_MAIN:latest
                        docker push $DOCKER_IMAGE_MAIN:latest
                    """
                withCredentials([usernamePassword(credentialsId: "${DOCKER_CREDENTIALS_ID}", usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh '''
                        echo "$DOCKER_PASS" | docker login --username "$DOCKER_USER" --password-stdin
                        docker build -t marijastopa/main-jenkins:latest .
                        docker push marijastopa/main-jenkins:latest
                    '''
                }
            }
        }
    }
    post {
        always {
            echo "Cleaning up Docker images..."
            sh """
                docker ps -a -q | xargs docker rm -f || true
                docker images -f dangling=true -q | xargs docker rmi -f || true
            """
        }
        failure {
            echo 'Pipeline failed. Check logs for errors.'
            echo 'Cleaning up Docker images...'
            sh '''
                docker ps -a -q | xargs --no-run-if-empty docker rm -f
                docker images -f dangling=true -q | xargs --no-run-if-empty docker rmi -f
            '''
        }
        success {
            echo 'Pipeline executed successfully!'
        }
        failure {
            echo 'Pipeline failed. Check logs for errors.'
        }
    }
}
