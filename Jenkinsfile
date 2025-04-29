pipeline {
    agent any

    environment {
        IMAGE_TAG = '' // Will be set in 'Initialize'
        REGISTRY = "your-dockerhub-username" // Replace with actual DockerHub or Nexus repo
    }

    stages {
        stage('Initialize') {
            steps {
                script {
                    env.IMAGE_TAG = sh(script: "git rev-parse --short HEAD", returnStdout: true).trim()
                    echo "Image tag is: ${env.IMAGE_TAG}"
                }
            }
        }

        stage('Branch Specific Pipeline') {
            steps {
                script {
                    if (env.BRANCH_NAME == 'main') {
                        currentBuild.description = "Main branch build"
                        
                        // Main branch: Just build and push image
                        docker.withRegistry('




