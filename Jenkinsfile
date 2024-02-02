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
        
        stage('Create Docker Image (MR)') {
            when {
                expression {
                    // Execute this stage only for merge requests
                    return env.CHANGE_ID != null
                }
            }
            steps {
                script {
                    // Assuming Dockerfile is at the root of the spring-petclinic repo
                    def gitCommitShort = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
                    def dockerImageTag = "${DOCKER_REPO_MR}:${gitCommitShort}"

                    sh "docker build -t ${dockerImageTag} ."
                    sh "docker push ${dockerImageTag}"
                }
            }
        }
        
        stage('Create Docker Image (Main)') {
            when {
                expression {
                    // Execute this stage only for the main branch
                    return env.BRANCH_NAME == 'main'
                }
            }
            steps {
                script {
                    // Assuming Dockerfile is at the root of the spring-petclinic repo
                    def gitCommitShort = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
                    def dockerImageTag = "${DOCKER_REPO_MAIN}:${gitCommitShort}"

                    sh "docker build -t ${dockerImageTag} ."
                    sh "docker push ${dockerImageTag}"
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

