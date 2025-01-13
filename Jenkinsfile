pipeline {
    agent any
    environment {
        DOCKER_REPO_MAIN = 'marijastopa/main-jenkins'
        DOCKER_REPO_MR = 'marijastopa/mr-jenkins'
        GIT_COMMIT_SHORT = sh(script: "git rev-parse --short HEAD", returnStdout: true).trim()
        DOCKER_IMAGE_MAIN = 'marijastopa/main-jenkins'
        DOCKER_IMAGE_MR = 'marijastopa/mr-jenkins'
    }
    stages {
        stage('Prepare') {
            steps {
                echo "Branch: ${env.BRANCH_NAME}"
                echo "Commit: ${GIT_COMMIT_SHORT}"
                echo "Preparing environment..."
                script {
                    BRANCH_NAME = env.BRANCH_NAME ?: 'main'
                    COMMIT = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
                    echo "Branch: ${BRANCH_NAME}"
                    echo "Commit: ${COMMIT}"
                }
            }
        }
        // Jobs for Merge Requests
        stage('Checkstyle') {
            when {
                expression {
                    return env.BRANCH_NAME != 'main'
                    BRANCH_NAME != 'main'
                }
            }
            steps {
                echo "Running Checkstyle..."
                sh './gradlew checkstyleMain checkstyleTest'
                archiveArtifacts artifacts: '**/build/reports/checkstyle/*.xml', allowEmptyArchive: true
                echo "Checkstyle completed and reports archived"
            }
        }
        stage('Test') {
            when {
                expression {
                    return env.BRANCH_NAME != 'main'
                    BRANCH_NAME != 'main'
                }
            }
            steps {
                echo "Running Tests..."
                echo "Running tests..."
                sh './gradlew test'
                junit '**/build/test-results/test/*.xml'
            }
        }
        stage('Build without Tests') {
            when {
                expression {
                    return env.BRANCH_NAME != 'main'
                    BRANCH_NAME != 'main'
                }
            }
            steps {
                echo "Building without running tests..."
                echo "Building application (without tests)..."
                sh './gradlew clean build -x test'
            }
        }
        stage('Docker Build & Push (Merge Request)') {
            when {
                expression {
                    return env.BRANCH_NAME != 'main'
                    BRANCH_NAME != 'main'
                }
            }
            steps {
                echo "Building Docker image for MR..."
                sh "docker build -t ${DOCKER_REPO_MR}:${GIT_COMMIT_SHORT} ."
                withCredentials([string(credentialsId: 'dockerhub-credentials', variable: 'DOCKER_PASS')]) {
                    sh '''
                        echo $DOCKER_PASS | docker login -u marijastopa --password-stdin
                        docker push ${DOCKER_REPO_MR}:${GIT_COMMIT_SHORT}
                        docker tag ${DOCKER_REPO_MR}:${GIT_COMMIT_SHORT} ${DOCKER_REPO_MR}:latest
                        docker push ${DOCKER_REPO_MR}:latest
                    '''
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
        // Jobs for Main Branch
        stage('Docker Build & Push (Main Branch)') {
            when {
                branch 'main'
            }
            steps {
                echo "Building Docker image for main branch..."
                sh "docker build -t ${DOCKER_REPO_MAIN}:${GIT_COMMIT_SHORT} ."
                withCredentials([string(credentialsId: 'dockerhub-credentials', variable: 'DOCKER_PASS')]) {
                    sh '''
                        echo $DOCKER_PASS | docker login -u marijastopa --password-stdin
                        docker push ${DOCKER_REPO_MAIN}:${GIT_COMMIT_SHORT}
                        docker tag ${DOCKER_REPO_MAIN}:${GIT_COMMIT_SHORT} ${DOCKER_REPO_MAIN}:latest
                        docker push ${DOCKER_REPO_MAIN}:latest
                    '''
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
            sh 'docker rmi $(docker images -f "dangling=true" -q) || true'
        }
        success {
            echo "Pipeline completed successfully!"
            sh """
                docker ps -a -q | xargs docker rm -f || true
                docker images -f dangling=true -q | xargs docker rmi -f || true
            """
        }
        failure {
            echo "Pipeline failed. Check logs for errors."
            echo 'Pipeline failed. Check logs for errors.'
        }
        success {
            echo 'Pipeline executed successfully!'
        }
    }
}
