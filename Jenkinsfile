pipeline {
    triggers {
        githubPush()
    }
    agent any
    environment {
        APP_NAME    = "petclinic"
        DOCKER_USER = "rizjosel"
        IMAGE_TAG   = "${BUILD_NUMBER}"
        IMAGE_NAME  = "${DOCKER_USER}/${APP_NAME}:${IMAGE_TAG}"
    }
    stages {

        stage('List Workspace') {
            steps {
                sh 'ls -l'
            }
        }

        stage('Build Application (Gradle)') {
            steps {
                sh './gradlew clean build -x test'
                archiveArtifacts artifacts: 'build/libs/*.jar', fingerprint: true
                junit 'build/test-results/test/*.xml'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh "docker build -t ${IMAGE_NAME} ."
            }
        }

        stage('Docker Push') {
            steps {
                withCredentials([string(credentialsId: 'dockerhub-creds', variable: 'DOCKER_TOKEN')]) {
                    sh """
                        echo \$DOCKER_TOKEN | docker login -u ${DOCKER_USER} --password-stdin
                        docker push ${IMAGE_NAME}
                        docker logout
                    """
                }
            }
        }
    }
}
