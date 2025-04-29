pipeline {
    agent any

    environment {
        IMAGE_TAG = sh(script: "git rev-parse --short HEAD", returnStdout: true).trim()
        REGISTRY = "your-dockerhub-username" // Or Nexus repo URL
    }

    stages {
        stage('Check Branch') {
            steps {
                script {
                    if (env.BRANCH_NAME == 'main') {
                        currentBuild.description = "Main branch build"
                        buildMainPipeline()
                    } else {
                        currentBuild.description = "Merge request build"
                        buildMRPipeline()
                    }
                }
            }
        }
    }
}

def buildMainPipeline() {
    stage('Docker Build and Push') {
        steps {
            script {
                docker.withRegistry('https://index.docker.io/v1/', 'docker-hub-credentials-id') {
                    def app = docker.build("${env.REGISTRY}/main:${env.IMAGE_TAG}")
                    app.push()
                }
            }
        }
    }
}

def buildMRPipeline() {
    stage('Checkstyle') {
        steps {
            sh 'gradle checkstyleMain checkstyleTest'
            archiveArtifacts artifacts: '**/build/reports/checkstyle/*.xml', allowEmptyArchive: true
        }
    }

    stage('Test') {
        steps {
            sh 'gradle test'
        }
    }

    stage('Build (No Tests)') {
        steps {
            sh 'gradle build -x test'
        }
    }

    stage('Docker Build and Push') {
        steps {
            script {
                docker.withRegistry('https://index.docker.io/v1/', 'docker-hub-credentials-id') {
                    def app = docker.build("${env.REGISTRY}/mr:${env.IMAGE_TAG}")
                    app.push()
                }
            }
        }
    }
}


