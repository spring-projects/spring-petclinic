pipeline {
    agent any
    tools {
        maven 'Maven 3' // Global Tool Configuration
    }
    environment {
        DOCKERHUB_USERNAME = credentials('dockerhub') 
        DOCKERHUB_PASSWORD = credentials('dockerhub') 
        DOCKER_IMAGE_MR = "marijastopa/mr-jenkins" 
        DOCKER_IMAGE_MAIN = "marijastopa/main-jenkins" 
    }
    stages {
        stage('Checkstyle') {
            steps {
                echo 'Running Checkstyle...'
                sh 'mvn checkstyle:checkstyle'
                archiveArtifacts artifacts: '**/target/site/checkstyle.html', fingerprint: true
            }
        }
        stage('Test') {
            steps {
                echo 'Running Tests...'
                sh 'mvn test'
            }
        }
        stage('Build') {
            steps {
                echo 'Building application without tests...'
                sh 'mvn clean package -DskipTests'
            }
        }
        stage('Build and Push Docker Image (Merge Request)') {
            when {
                branch 'develop' 
            }
            steps {
                script {
                    def commitHash = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
                    withDockerRegistry([credentialsId: 'dockerhub', url: '']) {
                        def image = docker.build("${DOCKER_IMAGE_MR}:${commitHash}")
                        image.push()
                    }
                }
            }
        }
        stage('Build and Push Docker Image (Main)') {
            when {
                branch 'main' 
            }
            steps {
                script {
                    def commitHash = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
                    withDockerRegistry([credentialsId: 'dockerhub', url: '']) {
                        def image = docker.build("${DOCKER_IMAGE_MAIN}:${commitHash}")
                        image.push()
                    }
                }
            }
        }
    }
}
