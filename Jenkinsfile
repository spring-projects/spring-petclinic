pipeline {
    agent {
        label 'mavenbuilder'
    }
    environment {
        DOCKER_IMAGE_NAME = 'spring-petclinic'
        DOCKER_REPO = 'mr'
    }
    stages {
        stage ('Checkstyle') {
            steps {
                script {
                    echo 'RUNNING CHECKSTYLES...'
                    sh 'mvn checkstyle:checkstyle'
                }
            }
        }
        stage ('Test') {
            steps {
                script {
                    echo 'RUNNING TESTS...'
                    sh 'mvn test'
                }
            }
        }
        stage ('Build') {
            steps {
                script {
                    echo 'BUILDING ARTIFACTS...'
                    sh 'mvn clean package'
                }
            }
        }
        stage('Build Docker Image') {
            steps {
                script {
                    def GIT_COMMIT_SHORT = sh(returnStdout: true, script: 'git rev-parse --short HEAD').trim()
                    env.DOCKER_TAG = "${DOCKER_IMAGE_NAME}:${GIT_COMMIT_SHORT}"
                    sh "docker build -t ${DOCKER_TAG} ."
                }
            }
        }        
        stage('Push Docker Image') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'docker_hub_login', passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME')]) {
                    script {
                        sh "docker login -u ${DOCKER_USERNAME} -p ${DOCKER_PASSWORD}"
                        sh "docker push ${DOCKER_TAG}"
                    }
                }
            }
        }
    }
}
