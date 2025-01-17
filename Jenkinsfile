pipeline {
    agent any
    environment {
        DOCKER_CREDENTIALS_ID = 'dockerhub-credentials' // ID DockerHub
    }
    stages {
        stage('Checkstyle') {
            when {
                expression { env.BRANCH_NAME != 'main' }
            }
            steps {
                sh './mvnw checkstyle:checkstyle'
                archiveArtifacts artifacts: 'target/checkstyle-result.xml', fingerprint: true
            }
        }
        stage('Test') {
            when {
                expression { env.BRANCH_NAME != 'main' }
            }
            steps {
                sh './mvnw test'
            }
        }
        stage('Build') {
            when {
                expression { env.BRANCH_NAME != 'main' }
            }
            steps {
                sh './mvnw clean package -DskipTests'
            }
        }
        stage('Create Docker Image') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: "${DOCKER_CREDENTIALS_ID}", usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                        def repoName = env.BRANCH_NAME == 'main' ? 'main-jenkins' : 'mr-jenkins'
                        def tag = env.GIT_COMMIT?.substring(0, 7) ?: 'latest'
                        
                        // Build Docker image
                        sh """
                            docker login -u \$DOCKER_USERNAME --password \$DOCKER_PASSWORD
                            docker build -t marijastopa/${repoName}:${tag} .
                        """
                        
                        // Tag 'latest' for main branch
                        if (env.BRANCH_NAME == 'main') {
                            sh """
                                docker tag marijastopa/${repoName}:${tag} marijastopa/${repoName}:latest
                                docker push marijastopa/${repoName}:latest
                            """
                        }
                        
                        // Push SHA tag
                        sh """
                            docker push marijastopa/${repoName}:${tag}
                        """
                    }
                }
            }
        }
    }
    post {
        always {
            echo "Pipeline execution completed."
        }
    }
}
