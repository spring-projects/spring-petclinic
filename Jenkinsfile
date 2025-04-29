pipeline {
    agent any

    environment {
        IMAGE_TAG = '' // We'll set this inside steps
        REGISTRY = "your-dockerhub-username" // Replace with your actual DockerHub username or Nexus repo
    }

    stages {
        stage('Initialize') {
            steps {
                script {
                    def IMAGE_TAG = sh(script: "git rev-parse --short HEAD", returnStdout: true).trim()
                    env.IMAGE_TAG = IMAGE_TAG // Save it to env for next stages
                    echo "Image tag is: ${env.IMAGE_TAG}"
                }
            }
        }

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
    stage('Docker Build and Push for Main') {
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
            script {
                sh 'gradle checkstyleMain checkstyleTest'
                archiveArtifacts artifacts: '**/build/reports/checkstyle/*.xml', allowEmptyArchive: true
            }
        }
    }

    stage('Test') {
        steps {
            script {
                sh 'gradle test'
            }
        }
    }

    stage('Build (No Tests)') {
        steps {
            script {
                sh 'gradle build -x test'
            }
        }
    }

    stage('Docker Build and Push for MR') {
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



