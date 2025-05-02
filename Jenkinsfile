pipeline {
    agent any
    
    tools {
        maven 'Maven'
    }
    
    environment {
        DOCKER_HUB_CREDENTIALS = credentials('docker-hub-credentials')
        DOCKER_HUB_USERNAME = "${DOCKER_HUB_CREDENTIALS_USR}"
        GIT_COMMIT_SHORT = sh(script: "git rev-parse --short HEAD", returnStdout: true).trim()
    }
    
    stages {
        stage('Merge Request Pipeline') {
            when {
                not { branch 'main' }
            }
            stages {
                stage('Checkstyle') {
                    steps {
                        sh 'mvn checkstyle:checkstyle'
                    }
                    post {
                        always {
                            archiveArtifacts artifacts: 'target/checkstyle-result.xml', allowEmptyArchive: true
                        }
                    }
                }
                
                stage('Test') {
                    steps {
                        sh 'mvn test'
                    }
                    post {
                        always {
                            junit 'target/surefire-reports/*.xml'
                        }
                    }
                }
                
                stage('Build') {
                    steps {
                        sh 'mvn clean package -DskipTests'
                    }
                    post {
                        success {
                            archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                        }
                    }
                }
                
                stage('Create Docker Image') {
                    steps {
                        script {
                            try {
                                sh 'echo $DOCKER_HUB_CREDENTIALS_PSW | docker login -u $DOCKER_HUB_CREDENTIALS_USR --password-stdin'
                                def imageName = "${DOCKER_HUB_USERNAME}/mr:${GIT_COMMIT_SHORT}"
                                sh "docker build -t ${imageName} -f Dockerfile ."
                                sh "docker push ${imageName}"
                            } catch (Exception e) {
                                error "Failed to build or push Docker image: ${e.message}"
                            } finally {
                                sh 'docker logout'
                            }
                        }
                    }
                }
            }
        }
        
        stage('Main Branch Pipeline') {
            when {
                branch 'main'
            }
            stages {
                stage('Build App') {
                    steps {
                        sh 'mvn clean package -DskipTests'
                    }
                    post {
                        success {
                            archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                        }
                    }
                }
                
                stage('Create Docker Image') {
                    steps {
                        script {
                            try {
                                sh 'echo $DOCKER_HUB_CREDENTIALS_PSW | docker login -u $DOCKER_HUB_CREDENTIALS_USR --password-stdin'
                                
                                def imageNameWithCommit = "${DOCKER_HUB_USERNAME}/main:${GIT_COMMIT_SHORT}"
                                sh "docker build -t ${imageNameWithCommit} -f Dockerfile ."
                                
                                def imageNameLatest = "${DOCKER_HUB_USERNAME}/main:latest"
                                sh "docker tag ${imageNameWithCommit} ${imageNameLatest}"
                                
                                sh "docker push ${imageNameWithCommit}"
                                sh "docker push ${imageNameLatest}"
                            } catch (Exception e) {
                                error "Failed to build or push Docker image: ${e.message}"
                            } finally {
                                sh 'docker logout'
                            }
                        }
                    }
                }
            }
        }
    }
    
    post {
        always {
            cleanWs()
        }
    }
}