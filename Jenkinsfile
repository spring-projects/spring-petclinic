pipeline {
    agent any

    environment {
        DOCKER_REPO_MAIN = 'prathushadevijs/main'
        DOCKER_REPO_MR = 'prathushadevijs/mr'
        DOCKER_CREDENTIALS = 'b1305615-4b2e-42e3-97ad-c87166d45f54'
    }

    stages {
        stage('Checkout Code') {
            steps {
                script {
                    sh 'git fetch --depth=1'
                    checkout scm
                }
            }
        }

        stage('Checkstyle') {
            steps {
                script {
                    // Ensure Docker can run properly
                    docker.image('maven:3.8.4-openjdk-17-slim').inside {
                        sh 'mvn checkstyle:checkstyle'
                    }
                    archiveArtifacts allowEmptyArchive: true, artifacts: '**/checkstyle-result.xml'
                }
            }
        }

        stage('Test') {
            steps {
                script {
                    docker.image('maven:3.8.4-openjdk-17-slim').inside {
                        sh 'mvn test'
                    }
                }
            }
        }

        stage('Build') {
            steps {
                script {
                    docker.image('maven:3.8.4-openjdk-17-slim').inside {
                        sh 'mvn clean package -DskipTests'
                    }
                }
            }
        }

        stage('Create Docker Image for Merge Request') {
            when {
                branch 'mr/*'
            }
            steps {
                script {
                    def commitHash = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
                    docker.withRegistry('https://index.docker.io/v1/', DOCKER_CREDENTIALS) {
                        sh "docker build -t ${DOCKER_REPO_MR}:${commitHash} ."
                        sh "docker push ${DOCKER_REPO_MR}:${commitHash}"
                    }
                }
            }
        }

        stage('Create Docker Image for Main Branch') {
            when {
                branch 'main'
            }
            steps {
                script {
                    def commitHash = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
                    docker.withRegistry('https://index.docker.io/v1/', DOCKER_CREDENTIALS) {
                        sh "docker build -t ${DOCKER_REPO_MAIN}:${commitHash} ."
                        sh "docker push ${DOCKER_REPO_MAIN}:${commitHash}"
                    }
                }
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}
