pipeline {
    agent any

    environment {
        DOCKERHUB_CREDENTIALS = credentials('dockerhub-credentials')
        IMAGE_NAME_MAIN = "prankumar313/main"
        IMAGE_NAME_MR = "prankumar313/mr"
    }

    stages {
        stage('Check Git Branch') {
            steps {
                script {
                    BRANCH_NAME = env.BRANCH_NAME ?: sh(script: 'git rev-parse --abbrev-ref HEAD', returnStdout: true).trim()
                    IS_MR = BRANCH_NAME != 'main'
                    COMMIT_HASH = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
                }
            }
        }

        stage('Checkstyle') {
            when {
                expression { IS_MR }
            }
            steps {
                sh './gradlew checkstyleMain'
            }
            post {
                always {
                    archiveArtifacts artifacts: 'build/reports/checkstyle/*.html', allowEmptyArchive: true
                }
            }
        }

        stage('Test') {
            when {
                expression { IS_MR }
            }
            steps {
                sh './gradlew test'
            }
        }

        stage('Build') {
            when {
                expression { IS_MR }
            }
            steps {
                sh './gradlew build -x test'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    IMAGE_TAG = "${IS_MR ? IMAGE_NAME_MR : IMAGE_NAME_MAIN}:${COMMIT_HASH}"
                    sh "docker buildx build -t ${IMAGE_TAG} ."
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    sh "echo ${DOCKERHUB_CREDENTIALS_PSW} | docker login -u ${DOCKERHUB_CREDENTIALS_USR} --password-stdin"
                    sh "docker push ${IMAGE_TAG}"
                }
            }
        }
    }
}
