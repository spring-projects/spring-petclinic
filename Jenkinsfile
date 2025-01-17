pipeline {
    agent any

    environment {
        GIT_COMMIT_SHORT = sh(script: "git rev-parse --short HEAD", returnStdout: true).trim()
        DOCKER_CREDENTIALS_ID = 'dockerhub-credentials' 
        DOCKERHUB_USER = 'marijastopa'
        DOCKERHUB_REPO_MAIN = 'main-jenkins'
        DOCKERHUB_REPO_MR = 'mr-jenkins'
    }

    stages {
        stage('Checkstyle') {
            when {
                not {
                    branch 'main'
                }
            }
            steps {
                echo 'Running Checkstyle...'
                sh './mvnw checkstyle:checkstyle'
                archiveArtifacts artifacts: 'target/site/checkstyle.html', allowEmptyArchive: true
            }
        }

        stage('Test') {
            when {
                not {
                    branch 'main'
                }
            }
            steps {
                echo 'Running Tests...'
                sh './mvnw clean test'
            }
        }

        stage('Build') {
            when {
                not {
                    branch 'main'
                }
            }
            steps {
                echo 'Building application...'
                sh './mvnw clean package -DskipTests'
            }
        }

        stage('Create Docker Image') {
            steps {
                echo 'Creating Docker Image...'
                script {
                    def dockerRepo = BRANCH_NAME == 'main' ? DOCKERHUB_REPO_MAIN : DOCKERHUB_REPO_MR
                    def imageTag = "${DOCKERHUB_USER}/${dockerRepo}:${GIT_COMMIT_SHORT}"
                    
                    withCredentials([usernamePassword(credentialsId: DOCKER_CREDENTIALS_ID, usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                        sh """
                            echo "$PASSWORD" | docker login -u "$USERNAME" --password-stdin
                            docker build -t ${imageTag} .
                            docker push ${imageTag}
                            docker logout
                        """
                    }
                }
            }
        }
    }
}
