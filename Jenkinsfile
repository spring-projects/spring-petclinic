pipeline {
    agent any
    environment {
        DOCKER_REPO_MAIN = 'marijastopa/main-jenkins'
        DOCKER_REPO_MR = 'marijastopa/mr-jenkins'
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

        // Merge Request Pipeline Stages
        stage('Checkstyle') {
            when {
                expression { BRANCH_NAME != 'main' }
            }
            steps {
                echo "Running Checkstyle..."
                sh './gradlew checkstyleMain checkstyleTest'
                archiveArtifacts artifacts: '**/build/reports/checkstyle/*.xml', allowEmptyArchive: true
            }
        }
        stage('Test') {
            when {
                expression { BRANCH_NAME != 'main' }
            }
            steps {
                echo "Running Tests..."
                sh './gradlew test'
                junit '**/build/test-results/test/*.xml'
            }
        }
        stage('Build without Tests') {
            when {
                expression { BRANCH_NAME != 'main' }
            }
            steps {
                echo "Building application without tests..."
                sh './gradlew clean build -x test'
            }
        }
        stage('Docker Build & Push (Merge Request)') {
            when {
                expression { BRANCH_NAME != 'main' }
            }
            steps {
                echo "Building Docker image for merge request..."
                withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh """
                        docker build -t ${DOCKER_REPO_MR}:${COMMIT} .
                        echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                        docker push ${DOCKER_REPO_MR}:${COMMIT}
                        docker tag ${DOCKER_REPO_MR}:${COMMIT} ${DOCKER_REPO_MR}:latest
                        docker push ${DOCKER_REPO_MR}:latest
                    """
                }
            }
        }

        // Main Branch Pipeline Stage
        stage('Docker Build & Push (Main Branch)') {
            when {
                branch 'main'
            }
            steps {
                echo "Building Docker image for main branch..."
                withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh """
                        docker build -t ${DOCKER_REPO_MAIN}:${COMMIT} .
                        echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                        docker push ${DOCKER_REPO_MAIN}:${COMMIT}
                        docker tag ${DOCKER_REPO_MAIN}:${COMMIT} ${DOCKER_REPO_MAIN}:latest
                        docker push ${DOCKER_REPO_MAIN}:latest
                    """
                }
            }
        }
    }
    post {
        always {
            echo "Cleaning up Docker images..."
            sh """
                docker ps -a -q | xargs --no-run-if-empty docker rm -f || true
                docker images -f dangling=true -q | xargs --no-run-if-empty docker rmi -f || true
            """
        }
        success {
            echo "Pipeline completed successfully!"
        }
        failure {
            echo "Pipeline failed. Check logs for errors."
        }
    }
}
