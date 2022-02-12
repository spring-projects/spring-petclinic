pipeline {
    agent any
    tools { 
        maven 'M3' 
    }
    options {
        buildDiscarder logRotator( 
            daysToKeepStr: '2', 
            numToKeepStr: '2'
        )
    }
    environment {
        DOCKERHUB_USERNAME = "kunchalavikram"
        JOB_NAME = "spring-petclinic"
        APP_NAME = "spring-petclinic"
        IMAGE_TAG = "${BUILD_NUMBER}"
        IMAGE_NAME = "${DOCKERHUB_USERNAME}" + "/" + "${APP_NAME}"
        REGISTRY_CREDS = 'dockerhub'
    }
    stages {
        stage('Cleanup Workspace') {
            steps {
                script {
                    cleanWs()
                    sh """
                    echo "Cleaned Up Workspace for ${JOB_NAME}"
                    """
                }
            }
        }
        stage('Checkout SCM'){
            steps {
                git url: 'https://github.com/kunchalavikram1427/spring-petclinic.git',
                branch: 'main'
            }
        }
        stage('Code Build') {
            steps {
                 sh "mvn -Dmaven.test.failure.ignore=true clean package"
            }
            post {
                success {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }
        stage('Build Docker Image'){
            steps {
                script{
                    docker_image = docker.build "${IMAGE_NAME}"
                }
            }
        }
        stage('Push Docker Image'){
            steps {
                script{
                    docker.withRegistry('', REGISTRY_CREDS ){
                        docker_image.push("${BUILD_NUMBER}")
                        docker_image.push('latest')
                    }
                }
            }
        }
        stage('Run Docker Image'){
            steps {
                sh "docker rm -f petclinic || true"
                sh "docker run -d -p 8181:8080 --name petclinic ${IMAGE_NAME}:${IMAGE_TAG}"
            }
        }  
        stage('Delete Docker Images'){
            when {
                expression { false }
            }
            steps {
                sh "docker rmi ${IMAGE_NAME}:${IMAGE_TAG}"
                sh "docker rmi ${IMAGE_NAME}:latest"
            }
        }
    }   
}
