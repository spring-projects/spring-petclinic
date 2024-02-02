pipeline {
    agent any
    
    environment {
        MAVEN_HOME = tool 'M3' // Assuming your Maven tool in Jenkins is named "M3"
        DOCKER_REPO_MR = "iancumatei67/mr" // Change to your Docker registry/repository for merge requests
        DOCKER_REPO_MAIN = "iancumatei67/main" // Change to your Docker registry/repository for main branch
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
        
        stage('Build Docker Image') {
            when {
                branch 'main'
            }
            steps {
                script {
                    app = docker.build("iancumatei67/main")
                    app.inside {
                        sh 'echo $(curl localhost:8080)'
                    }
                }
            }
        }
        stage('Push Docker Image') {
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
    
    post {
        always {
            cleanWs() // Clean workspace after the pipeline execution
        }
    }
}
}

