pipeline {
    agent {
        docker {
            image 'docker:20.10.8-dind' // Docker DinD image
            args '--privileged -v /var/lib/docker:/var/lib/docker'
        }
    }

    environment {
        DOCKER_IMAGE_NAME = "mr-jenkins" 
        DOCKER_MAIN_REPO = "main-jenkins"
        SHORT_COMMIT = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim() 
        DOCKERHUB_REPO = "marijastopa"
    }

    stages {
        stage('Checkstyle') {
            when {
                not { branch 'main' } // merge request
            }
            steps {
                sh './mvnw checkstyle:checkstyle'
                archiveArtifacts artifacts: 'target/site/checkstyle.html', fingerprint: true
            }
        }
        stage('Test') {
            when {
                not { branch 'main' } // merge request
            }
            steps {
                sh './mvnw clean test'
            }
        }
        stage('Build') {
            when {
                not { branch 'main' } // merge request
            }
            steps {
                sh './mvnw clean package -DskipTests'
            }
        }
        stage('Create Docker Image') {
            steps {
                script {
                    def repoName = BRANCH_NAME == 'main' ? DOCKER_MAIN_REPO : DOCKER_IMAGE_NAME
                    def tag = "${DOCKERHUB_REPO}/${repoName}:${SHORT_COMMIT}"
                    sh "docker build -t ${tag} ."
                }
            }
        }
        stage('Push Docker Image') {
            steps {
                script {
                    def repoName = BRANCH_NAME == 'main' ? DOCKER_MAIN_REPO : DOCKER_IMAGE_NAME
                    def tag = "${DOCKERHUB_REPO}/${repoName}:${SHORT_COMMIT}"
                    withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                        sh "echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin"
                        sh "docker push ${tag}"
                    }
                }
            }
        }
    }

    post {
        always {
            echo 'Pipeline completed.'
        }
        success {
            echo 'Pipeline completed successfully.'
        }
        failure {
            echo 'Pipeline failed.'
        }
    }
}
