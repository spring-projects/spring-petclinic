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
                            def shortCommit = sh(script: "git rev-parse --short HEAD", returnStdout: true).trim()
                            
                            docker.withRegistry("http://host.docker.internal:${env.NEXUS_PORT_MR}", 'nexus-credentials') {
                                def customImage = docker.build("host.docker.internal:${env.NEXUS_PORT_MR}/${env.NEXUS_REPO_MR}:${shortCommit}", "-f Dockerfile.multi .")
                                customImage.push()
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
                            def shortCommit = sh(script: "git rev-parse --short HEAD", returnStdout: true).trim()
                            
                            docker.withRegistry("http://host.docker.internal:${env.NEXUS_PORT_MAIN}", 'nexus-credentials') {
                                def customImage = docker.build("host.docker.internal:${env.NEXUS_PORT_MAIN}/${env.NEXUS_REPO_MAIN}:${shortCommit}", "-f Dockerfile.multi .")
                                customImage.push()
                            }
                        }
                    }
                }
            }
        }
    }
}