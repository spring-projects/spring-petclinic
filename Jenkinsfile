pipeline {
    agent any

    environment {
        DOCKER_REPO_MAIN = 'prathushadevijs/main'  // Replace with your Docker Hub username or Nexus repository URL
        DOCKER_REPO_MR = 'prathushadevijs/mr'      // Replace with your Docker Hub username or Nexus repository URL
        DOCKER_CREDENTIALS = 'b1305615-4b2e-42e3-97ad-c87166d45f54'
    }

    stages {
        stage('Checkstyle') {
            steps {
                script {
                    // Run Maven Checkstyle plugin and save the report as an artifact
                    sh 'mvn checkstyle:checkstyle'
                    archiveArtifacts allowEmptyArchive: true, artifacts: '**/checkstyle-result.xml'
                }
            }
        }

        stage('Test') {
            steps {
                script {
                    // Run tests with Maven (or Gradle if preferred)
                    sh 'mvn test'
                }
            }
        }

        stage('Build') {
            steps {
                script {
                    // Build the application without running tests
                    sh 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Create Docker Image for Merge Request') {
            when {
                branch 'mr/*'  // This will trigger the stage for a Merge Request branch
            }
            steps {
                script {
                    // Build Docker image for the merge request
                    def commitHash = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
                    sh "docker build -t ${DOCKER_REPO_MR}:${commitHash} ."
                    sh "docker push ${DOCKER_REPO_MR}:${commitHash}"
                }
            }
        }

        stage('Create Docker Image for Main Branch') {
            when {
                branch 'main'  // This will trigger the stage for the main branch
            }
            steps {
                script {
                    // Build Docker image for the main branch
                    def commitHash = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
                    sh "docker build -t ${DOCKER_REPO_MAIN}:${commitHash} ."
                    sh "docker push ${DOCKER_REPO_MAIN}:${commitHash}"
                }
            }
        }
    }

    post {
        always {
            cleanWs()  // Clean workspace after the pipeline run
        }
    }
}
