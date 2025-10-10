pipeline {
    agent any

    environment {
        IMAGE_NAME = "spring-petclinic"
        REGISTRY_MAIN = "localhost:8084/spring-petclinic"
        REGISTRY_MR   = "localhost:8083/spring-petclinic"
    }

    stages {

        stage('Checkstyle') {
            steps {
                echo "Running Checkstyle..."
                sh "./mvnw checkstyle:checkstyle -B"
                archiveArtifacts artifacts: '**/target/checkstyle-result.xml', fingerprint: true
            }
        }

        stage('Test') {
            steps {
                echo "Running unit tests..."
                sh "./mvnw test -B"
                junit '**/target/surefire-reports/*.xml'
            }
        }

        stage('Build JAR') {
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

        stage('Push to Nexus Registry') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'nexus', usernameVariable: 'NEXUS_USER', passwordVariable: 'NEXUS_PASS')]) {

                        // Login to both repos (ignore failures)
                        sh "echo $NEXUS_PASS | docker login localhost:8083 -u $NEXUS_USER --password-stdin || true"
                        sh "echo $NEXUS_PASS | docker login localhost:8084 -u $NEXUS_USER --password-stdin || true"

                        BRANCH_NAME = sh(script: "git rev-parse --abbrev-ref HEAD", returnStdout: true).trim()

                        if (BRANCH_NAME == "main" || BRANCH_NAME == "master") {
                            TARGET_REGISTRY = "${REGISTRY_MAIN}"
                        } else {
                            TARGET_REGISTRY = "${REGISTRY_MR}"
                        }

                        echo "Tagging and pushing to ${TARGET_REGISTRY}:${IMAGE_TAG}"
                        sh """
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
