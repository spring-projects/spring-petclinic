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
        
        stage('Test Docker Image') {
            steps {
                // Run container for testing
                sh "docker run -d --name ${APP_NAME}-test -p 8080:8080 ${IMAGE_NAME}"

                // Wait for app to start
                sh "sleep 10"

                // Health check - if curl fails, this step will fail and stop the pipeline
                sh "curl -f http://localhost:8080/actuator/health"

                // Stop and remove test container
                sh "docker stop ${APP_NAME}-test && docker rm ${APP_NAME}-test"
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
