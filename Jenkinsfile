pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Checkstyle') {
            steps {
                script {
                    sh './gradlew checkstyleMain'
                }
            }
            post {
                success {
                    archiveArtifacts artifacts: 'build/reports/checkstyle/*.xml', allowEmptyArchive: true
                }
            }
        }
        stage('Unit Tests') {
            steps {
                script {
                    echo "Running unit tests..."
                    sh './gradlew test'
                }
            }
        }
        stage('Build without Tests') {
            steps {
                script {
                    sh './gradlew build -x test'
                }
            }
        }
        stage('Create Docker Image - MR') {
            when {
                branch 'PR-*'  // This will trigger for pull requests (merge requests)
            }
            steps {
                script {
                    // Build Docker image with the short Git commit hash as the tag
                    def gitCommit = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
                    sh "docker build -t ${DOCKER_REGISTRY}/mr/spring-petclinic:${gitCommit} ."
                    sh "docker login -u ${DOCKER_CREDENTIALS_USR} -p ${DOCKER_CREDENTIALS_PSW}"
                    sh "docker push ${DOCKER_REGISTRY}/mr/spring-petclinic:${gitCommit}"
                }
            }
        }

    }
}