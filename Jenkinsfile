pipeline {
    agent any

    environment {
        GIT_COMMIT = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
        DOCKER_CREDS = credentials('docker_key')
        DOCKER_REPO = ''
    }

    stages {
        stage('Set Docker Repository') {
            steps {
                script {
                    if (env.BRANCH_NAME == 'main') {
                        DOCKER_REPO = 'vkarpenko02/main'
                    } else {
                        DOCKER_REPO = 'vkarpenko02/mr'
                    }
                }
            }
        }

        stage('Checkstyle') {
            when { not { branch 'main' } } // Only for MR pipelines
            steps {
                sh 'mvn checkstyle:checkstyle'
                archiveArtifacts artifacts: 'target/site/checkstyle.html', allowEmptyArchive: true
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests'
                archiveArtifacts artifacts: 'target/*.jar', allowEmptyArchive: false
            }
        }

        stage('Create Docker Image') {
            steps {
                sh 'docker build -t ${DOCKER_REPO}:${GIT_COMMIT} .'
                withCredentials([usernamePassword(credentialsId: 'docker_key', usernameVariable: 'DOCKER_HUB_USER', passwordVariable: 'DOCKER_HUB_PASS')]) {
                    sh """
                        echo ${DOCKER_HUB_PASS} | docker login -u ${DOCKER_HUB_USER} --password-stdin
                        docker push ${DOCKER_REPO}:${GIT_COMMIT}
                    """
                }
            }
        }
    }
}
