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
                        docker.withRegistry('https://index.docker.io/v1/', 'docker-hub-credentials-id') {
                            def app = docker.build("${env.REGISTRY}/main:${env.IMAGE_TAG}")
                            app.push()
                        }

                    } else {
                        currentBuild.description = "Merge request build"
                        
                        // MR branch: Full build
                        sh 'gradle checkstyleMain checkstyleTest'
                        archiveArtifacts artifacts: '**/build/reports/checkstyle/*.xml', allowEmptyArchive: true

                        sh 'gradle test'
                        sh 'gradle build -x test'

                        docker.withRegistry('https://index.docker.io/v1/', 'docker-hub-credentials-id') {
                            def app = docker.build("${env.REGISTRY}/mr:${env.IMAGE_TAG}")
                            app.push()
                        }
                    }
                }
            }
        }
    }
}
