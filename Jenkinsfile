pipeline {
    agent none

    environment {
        DOCKERHUB_CREDS = credentials('dockerhub-cred')
        DOCKERHUB_NAMESPACE = 'maljkovica'
        MR_REPO = "${DOCKERHUB_NAMESPACE}/mr"
        MAIN_REPO = "${DOCKERHUB_NAMESPACE}/main"
    }

    stages {

        stage('Merge Request Pipeline') {
            when {
                changeRequest() 
            }
            stages {
                stage('Checkstyle') {
                    agent {
                        docker { image 'maven:3.9-eclipse-temurin-17' }
                    }
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
                    agent {
                        docker { image 'maven:3.9-eclipse-temurin-17' }
                    }
                    steps {
                        sh 'mvn test'
                    }
                }

                stage('Build') {
                    agent {
                        docker { image 'maven:3.9-eclipse-temurin-17' }
                    }
                    steps {
                        sh 'mvn package -DskipTests'
                        archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                    }
                }

                stage('Create Docker Image (mr)') {
                    agent {
                        docker {
                            image 'docker:24-cli'
                            args '-v /var/run/docker.sock:/var/run/docker.sock'
                        }
                    }
                    steps {
                        script {
                            def shortCommit = sh(script: "git rev-parse --short HEAD", returnStdout: true).trim()
                            def image = docker.build("${MR_REPO}:${shortCommit}")
                            docker.withRegistry('https://registry.hub.docker.com', 'dockerhub-cred') {
                                image.push()
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
                stage('Create Docker Image (main)') {
                    agent {
                        docker {
                            image 'docker:24-cli'
                            args '-v /var/run/docker.sock:/var/run/docker.sock'
                        }
                    }
                    steps {
                        script {
                            def shortCommit = sh(script: "git rev-parse --short HEAD", returnStdout: true).trim()
                            def image = docker.build("${MAIN_REPO}:${shortCommit}")
                            docker.withRegistry('https://registry.hub.docker.com', 'dockerhub-cred') {
                                image.push()
                                image.push('latest')
                            }
                        }
                    }
                }
            }
        }
    }
}