pipeline {

    agent {label 'agent-1'}

    environment {
        DOCKER_HUB_CREDENTIALS='docker-hub-credentials'
        MR_IMAGE_NAME='dejanakop/spring-petclinic-mr'
        MAIN_IMAGE_NAME='dejanakop/spring-petclinic-main'
        TAG=sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
    }
    
    stages {
        
        stage("Checkout") {
            steps {
                checkout scm
            }
        }

        stage("checkstyle") {
            when {
                changeRequest()
            }
            steps {
                echo "Checkstyle..."
                sh './gradlew checkstyleMain'
                archiveArtifacts artifacts: 'build/reports/checkstyle/*.xml', allowEmptyArchive: true
                sh 'ls -la'
            }
        }

        stage("test") {
            when {
                changeRequest()
            }
            steps {
                echo "Testing..."
                sh './gradlew clean test'
            }
        }
        
        stage("build") {
            when {
                changeRequest()
            }
            steps {
                echo "Building..."
                sh './gradlew clean build -x test -x checkstyleNohttp'
            }
        }

        stage("docker image (change request)") {
            when {
                changeRequest()
            }
            steps {
                echo "Building Docker image for change request..."
                sh 'docker build -t ${MR_IMAGE_NAME}:${TAG} .'
                withCredentials([usernamePassword(credentialsId: DOCKER_HUB_CREDENTIALS, usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                    sh 'docker login -u ${DOCKER_USERNAME} -p ${DOCKER_PASSWORD}'
                    sh 'docker push ${MR_IMAGE_NAME}:${TAG}'
                    sh 'docker logout'
                }
            }
        }

        stage("docker image (main)") {
            when {
                branch 'main'
            }
            steps {
                echo "Building Docker image for main..."
                sh 'docker build -t ${MAIN_IMAGE_NAME}:${TAG} .'
                withCredentials([usernamePassword(credentialsId: DOCKER_HUB_CREDENTIALS, usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                    sh 'docker login -u ${DOCKER_USERNAME} -p ${DOCKER_PASSWORD}'
                    sh 'docker push ${MAIN_IMAGE_NAME}:${TAG}'
                    sh 'docker logout'
                }
            }
        }

    }

}
