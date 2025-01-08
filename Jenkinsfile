pipeline {
    agent any
    environment {
        DOCKER_IMAGE_MAIN = 'marijastopa/main-jenkins'
        DOCKER_IMAGE_MR = 'marijastopa/mr-jenkins'
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
            }
            steps {
                echo "Running Checkstyle..."
                sh './gradlew checkstyleMain checkstyleTest'
                archiveArtifacts artifacts: '**/build/reports/checkstyle/*.xml', allowEmptyArchive: true
            }
        }
        stage('Test') {
            when {
                expression {
                    BRANCH_NAME != 'main'
                }
            }
            steps {
                echo "Running tests..."
                sh './gradlew test'
            }
        }
        stage('Build without Tests') {
            when {
                expression {
                    BRANCH_NAME != 'main'
                }
            }
            steps {
                echo "Building application (without tests)..."
                sh './gradlew clean build -x test'
            }
        }
        stage('Docker Build & Push (Merge Request)') {
            when {
                expression {
                    BRANCH_NAME != 'main'
                }
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
                }
            }
        }
        stage('Docker Build & Push (Main Branch)') {
            when {
                branch 'main'
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
        }
        success {
            echo 'Pipeline executed successfully!'
        }
    }
}
