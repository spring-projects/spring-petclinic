pipeline {
    agent any

    environment {
        IMAGE_NAME = "spring-petclinic"
        REGISTRY_MAIN = "tomaciobotaru12/spring-petclinic-main"
        REGISTRY_MR   = "tomaciobotaru12/spring-petclinic-mr"
    }

    stages {

        stage('Checkstyle') {
            when {
                expression { env.BRANCH_NAME != 'main' } // Skip for main
            }
            steps {
                echo "Running Checkstyle..."
                sh "./mvnw checkstyle:checkstyle -B"
                archiveArtifacts artifacts: '**/target/checkstyle-result.xml', fingerprint: true
            }
        }

        stage('Test') {
            when {
                expression { env.BRANCH_NAME != 'main' } // Skip for main
            }
            steps {
                echo "Running unit tests..."
                sh "./mvnw test -B"
                junit '**/target/surefire-reports/*.xml'
            }
        }

        stage('Build JAR') {
            when {
                expression { env.BRANCH_NAME != 'main' } // Skip for main
            }
            steps {
                echo "Building application JAR (without tests)..."
                sh "./mvnw clean package -DskipTests -B"
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    COMMIT_HASH = sh(script: "git rev-parse --short HEAD", returnStdout: true).trim()
                    IMAGE_TAG = "${COMMIT_HASH}"
                    echo "Building Docker image ${IMAGE_NAME}:${IMAGE_TAG}"
                    sh "docker build -t ${IMAGE_NAME}:${IMAGE_TAG} ."
                }
            }
        }

        stage('Push to Docker Repository') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'dockerhub', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {

                        if (env.BRANCH_NAME == 'main') {
                            TARGET_REGISTRY = "${REGISTRY_MAIN}"
                        } else {
                            TARGET_REGISTRY = "${REGISTRY_MR}"
                        }

                        echo "Tagging and pushing to ${TARGET_REGISTRY}:${IMAGE_TAG}"
                        sh """
                            echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin
                            docker tag ${IMAGE_NAME}:${IMAGE_TAG} ${TARGET_REGISTRY}:${IMAGE_TAG}
                            docker push ${TARGET_REGISTRY}:${IMAGE_TAG}
                        """
                    }
                }
            }
        }
    }

    post {
        always {
            echo "Cleaning up local Docker images..."
            sh "docker image prune -f || true"
        }
    }
}
