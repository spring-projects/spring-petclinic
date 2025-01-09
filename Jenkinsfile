pipeline {
    agent any
    environment {
        GRADLE_BUILD_IMAGE = "gradle:8.12.0-jdk17"
        DOCKER_BUILD_IMAGE = "docker:27"
        DOCKER_CREDENTIALS_ID = 'dockerhub-credentials'  // Jenkins credential ID for Docker Hub
    }
    stages {
        stage('Checkstyle') {
            when {
                expression { env.CHANGE_ID != null }  // Only run for merge requests
            }
            agent {
                docker { image "${GRADLE_BUILD_IMAGE}" }
            }
            steps {
                sh 'gradle checkstyleMain'
                archiveArtifacts artifacts: 'build/reports/checkstyle/**/*', allowEmptyArchive: true
            }
        }
        stage('Test') {
            when {
                expression { env.CHANGE_ID != null }  // Only run for merge requests
            }
            agent {
                docker { image "${GRADLE_BUILD_IMAGE}" }
            }
            steps {
                sh 'gradle test'
            }
        }
        stage('Build without Tests') {
            when {
                expression { env.CHANGE_ID != null }  // Only run for merge requests
            }
            agent {
                docker { image "${GRADLE_BUILD_IMAGE}" }
            }
            steps {
                sh 'gradle clean build -x test'
            }
        }
        stage('Docker Build & Push (Merge Request)') {
            when {
                expression { env.CHANGE_ID != null }  // Only run for merge requests
            }
            agent {
                docker { image "${DOCKER_BUILD_IMAGE}" }
            }
            steps {
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
                branch 'main'  // Only run for the main branch
            }
            agent {
                docker { image "${DOCKER_BUILD_IMAGE}" }
            }
            steps {
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
