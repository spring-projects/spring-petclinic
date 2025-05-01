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
                    agent {
                        docker {
                            image 'docker:dind'
                            args '-v /var/run/docker.sock:/var/run/docker.sock'
                        }
                    }
                    steps {
                        script {
                            def shortCommit = sh(script: "git rev-parse --short HEAD", returnStdout: true).trim()
                            withCredentials([usernamePassword(credentialsId: 'nexus-credentials',
                                usernameVariable: 'NEXUS_USERNAME',
                                passwordVariable: 'NEXUS_PASSWORD')]) {
                                sh "docker build -f Dockerfile.multi -t ${env.NEXUS_SERVER}:${env.NEXUS_PORT_MR}/${env.NEXUS_REPO_MR}:${shortCommit} ."
                                sh "docker login ${env.NEXUS_SERVER}:${env.NEXUS_PORT_MR} -u ${NEXUS_USERNAME} -p ${NEXUS_PASSWORD}"
                                sh "docker push ${env.NEXUS_SERVER}:${env.NEXUS_PORT_MR}/${env.NEXUS_REPO_MR}:${shortCommit}"
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
                    agent {
                        docker {
                            image 'docker:dind'
                            args '-v /var/run/docker.sock:/var/run/docker.sock'
                        }
                    }
                    steps {
                        script {
                            def shortCommit = sh(script: "git rev-parse --short HEAD", returnStdout: true).trim()
                            withCredentials([usernamePassword(credentialsId: 'nexus-credentials',
                                usernameVariable: 'NEXUS_USERNAME',
                                passwordVariable: 'NEXUS_PASSWORD')]) {
                                sh "docker build -f Dockerfile.multi -t ${env.NEXUS_SERVER}:${env.NEXUS_PORT_MAIN}/${env.NEXUS_REPO_MAIN}:${shortCommit} ."
                                sh "docker login ${env.NEXUS_SERVER}:${env.NEXUS_PORT_MAIN} -u ${NEXUS_USERNAME} -p ${NEXUS_PASSWORD}"
                                sh "docker push ${env.NEXUS_SERVER}:${env.NEXUS_PORT_MAIN}/${env.NEXUS_REPO_MAIN}:${shortCommit}"
                            }
                        }
                    }
                }
            }
        }
    }
}