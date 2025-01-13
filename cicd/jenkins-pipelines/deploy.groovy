pipeline {
    agent any
    parameters {
        string(name: 'ARTIFACT_VERSION', description: 'Version of the artifact to deploy')
    }
    environment {
        DOCKERHUB_USERNAME = "nalexx6"
    }
    stages {
        stage('Pull Docker Image') {
            steps {
                sh "docker pull ${DOCKERHUB_USERNAME}/petclinic:${ARTIFACT_VERSION}"
            }
        }
        stage('Run Docker Container') {
            steps {
                sh "docker run -d -p 80:8080 ${DOCKERHUB_USERNAME}/petclinic:${ARTIFACT_VERSION}"
            }
        }
    }
}
