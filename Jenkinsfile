pipeline {
    agent any

    environment {
        REPO_MAIN = "main"
        IMAGE_NAME = "spring-petclinic"
    }

    stages {
        stage ('Checkout') {
            when {
                expression { env.CHANGE_ID != null }
            }
            steps {
                checkout scm
            }
        }

        stage ('Checkstyle') {
            when {
                expression { env.CHANGE_ID != null }
            }
            steps {
                script {
                    sh 'mvn checkstyle:checkstyle'
                }
            }
            post {
                always {
                    archiveArtifacts artifacts: '**/checkstyle-result.xml'
                }
            }
        }

        stage ('Test') {
            when {
                expression { env.CHANGE_ID != null }
            }
            steps {
                script {
                    sh 'mvn test'
                }
            }
        }

        stage ('Build') {
            when {
                expression { env.CHANGE_ID != null }
            }
            steps {
                script {
                    sh 'mvn clean install -DskipTests'
                }
            }
        }

        stage ('Docker Image for MR') {
            when {
                expression { env.CHANGE_ID != null }
            }
            steps {
                script {
                    def shortCommit = env.GIT_COMMIT.substring(0, 7)
                    withCredentials([usernamePassword(
                        credentialsId: 'docker-credentials',
                        usernameVariable: 'DOCKER_CREDS_USR',
                        passwordVariable: 'DOCKER_CREDS_PSW'
                    )]) {
                        sh """
                        echo "${DOCKER_CREDS_PSW}" | docker login -u "${DOCKER_CREDS_USR}" --password-stdin
                        docker build -t ${DOCKER_CREDS_USR}/${IMAGE_NAME}:${shortCommit} .
                        docker tag ${DOCKER_CREDS_USR}/${IMAGE_NAME}:${shortCommit} ${DOCKER_CREDS_USR}/${IMAGE_NAME}:${shortCommit}
                        docker push ${DOCKER_CREDS_USR}/${IMAGE_NAME}:${shortCommit}
                        """
                    }
                }
            }
        }

        stage ('Docker Image for main branch') {
            when {
                expression {
                    env.BRANCH_NAME == 'main' || env.GIT_BRANCH?.contains('main')
                }
            }
            steps {
                script {
                    def shortCommit = env.GIT_COMMIT.substring(0, 7)
                    withCredentials([usernamePassword(
                        credentialsId: 'docker-credentials',
                        usernameVariable: 'DOCKER_HUB_USR',
                        passwordVariable: 'DOCKER_HUB_TOKEN'
                    )]) {
                        sh """
                        echo "${DOCKER_HUB_TOKEN}" | docker login -u "${DOCKER_HUB_USR}" --password-stdin
                        docker build -t ${DOCKER_HUB_USR}/${IMAGE_NAME}:${shortCommit} .
                        docker tag ${DOCKER_HUB_USR}/${IMAGE_NAME}:${shortCommit} ${DOCKER_HUB_USR}/${IMAGE_NAME}:${shortCommit}
                        docker push ${DOCKER_HUB_USR}/${IMAGE_NAME}:${shortCommit}
                        """
                    }
                }
            }
        }
    }
}
