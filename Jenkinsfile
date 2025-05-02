pipeline {
    agent any
    tools {
        maven 'Maven'
    }
    environment {
        DOCKER_HUB_CREDENTIALS = credentials('docker-hub-credentials')
        DOCKER_HUB_USERNAME = "${DOCKER_HUB_CREDENTIALS_USR}"
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
                            def shortCommit = sh(script: "git rev-parse --short HEAD", returnStdout: true).trim()
                            sh 'echo $DOCKER_HUB_CREDENTIALS_PSW | docker login -u $DOCKER_HUB_CREDENTIALS_USR --password-stdin'
                            def imageName = "${DOCKER_HUB_USERNAME}/mr:${shortCommit}"
                            sh "docker build -t ${imageName} -f Dockerfile.multi ."
                            sh "docker push ${imageName}"
                            sh 'docker logout'
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
                            def shortCommit = sh(script: "git rev-parse --short HEAD", returnStdout: true).trim()
                            
                            sh 'echo $DOCKER_HUB_CREDENTIALS_PSW | docker login -u $DOCKER_HUB_CREDENTIALS_USR --password-stdin'
                            def imageName = "${DOCKER_HUB_USERNAME}/main:${shortCommit}"
                            sh "docker build -t ${imageName} -f Dockerfile.multi ."
                            sh "docker push ${imageName}"
                            sh 'docker logout'
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