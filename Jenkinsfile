pipeline {
    agent any

    environment {
        DOCKER_REPO_MAIN = 'marijastopa/main-jenkins'
        DOCKER_REPO_MR = 'marijastopa/mr-jenkins'
        GIT_COMMIT_SHORT = sh(script: "git rev-parse --short HEAD", returnStdout: true).trim()
    }

    stages {
        stage('Prepare') {
            steps {
                echo "Branch: ${env.BRANCH_NAME}"
                echo "Commit: ${GIT_COMMIT_SHORT}"
            }
        }

        // Jobs for Merge Requests
        stage('Checkstyle') {
            when {
                expression {
                    return env.BRANCH_NAME != 'main'
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
                }
            }
            steps {
                echo "Running Tests..."
                sh './gradlew test'
                junit '**/build/test-results/test/*.xml'
            }
        }

        stage('Build without Tests') {
            when {
                expression {
                    return env.BRANCH_NAME != 'main'
                }
            }
            steps {
                echo "Building without running tests..."
                sh './gradlew clean build -x test'
            }
        }

        stage('Docker Build & Push (Merge Request)') {
            when {
                expression {
                    return env.BRANCH_NAME != 'main'
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
        }
        failure {
            echo "Pipeline failed. Check logs for errors."
        }
    }
}
