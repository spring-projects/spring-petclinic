pipeline {
    agent any
    tools {
        maven 'Maven'
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
                        	sh 'sudo chmod 666 /var/run/docker.sock || true'
                        	
                            def shortCommit = sh(script: "git rev-parse --short HEAD", returnStdout: true).trim()
                            
                            sh "docker build -f Dockerfile.multi -t host.docker.internal:8084/mr:${shortCommit} ."
                            
                            withCredentials([usernamePassword(credentialsId: 'nexus-credentials',
                                usernameVariable: 'NEXUS_USERNAME',
                                passwordVariable: 'NEXUS_PASSWORD')]) {
                                sh "echo \${NEXUS_PASSWORD} | docker login host.docker.internal:8084 -u \${NEXUS_USERNAME} --password-stdin"
                                sh "docker push host.docker.internal:8084/mr:${shortCommit}"
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
                        	sh 'sudo chmod 666 /var/run/docker.sock || true'

                            def shortCommit = sh(script: "git rev-parse --short HEAD", returnStdout: true).trim()
                            
                            sh "docker build -f Dockerfile.multi -t host.docker.internal:8083/main:${shortCommit} ."
                            
                            withCredentials([usernamePassword(credentialsId: 'nexus-credentials',
                                usernameVariable: 'NEXUS_USERNAME',
                                passwordVariable: 'NEXUS_PASSWORD')]) {
                                sh "echo \${NEXUS_PASSWORD} | docker login host.docker.internal:8083 -u \${NEXUS_USERNAME} --password-stdin"
                                sh "docker push host.docker.internal:8083/main:${shortCommit}"
                            }
                        }
                    }
                }
            }
        }
    }
}