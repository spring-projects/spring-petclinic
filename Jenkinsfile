pipeline {
   agent any
   environment {
       REGISTRY_MAIN = "localhost:8083/spring-petclinic"
       REGISTRY_MR = "localhost:8084/spring-petclinic"
       IMAGE_NAME = "spring-petclinic"
       MAVEN_HOME = tool 'maven' 

   }
   
    stages {

        stage('Checkstyle') {
            steps {
                echo "Running Checkstyle..."
                sh "${MAVEN_HOME}/bin/mvn checkstyle:checkstyle"
                archiveArtifacts artifacts: '**/target/checkstyle-result.xml', fingerprint: true
            }
        }

        stage('Test') {
            steps {
                echo "Running Tests..."
                sh "${MAVEN_HOME}/bin/mvn test"
                junit '**/target/surefire-reports/*.xml'
            }
        }

        stage('Build (no tests)') {
            steps {
                echo "Building application JAR..."
                sh "${MAVEN_HOME}/bin/mvn clean package -DskipTests"
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    COMMIT_HASH = sh(script: "git rev-parse --short HEAD", returnStdout: true).trim()
                    IMAGE_TAG = "${COMMIT_HASH}"
                    echo "Building Docker image with tag ${IMAGE_TAG}..."
                    sh "docker build -t ${IMAGE_NAME}:${IMAGE_TAG} ."
                }
            }
        }

        stage('Push to Nexus Registry') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'nexus', usernameVariable: 'NEXUS_USER', passwordVariable: 'NEXUS_PASS')]) {

                        // Login to Nexus
                        sh "echo $NEXUS_PASS | docker login localhost:8083 -u $NEXUS_USER --password-stdin || true"
                        sh "echo $NEXUS_PASS | docker login localhost:8084 -u $NEXUS_USER --password-stdin || true"

                        BRANCH_NAME = sh(script: "git rev-parse --abbrev-ref HEAD", returnStdout: true).trim()
                        if (BRANCH_NAME == "main" || BRANCH_NAME == "master") {
                            TARGET_REGISTRY = "${REGISTRY_MAIN}"
                        } else {
                            TARGET_REGISTRY = "${REGISTRY_MR}"
                        }

                        // Tag and push
                        echo "Pushing image to ${TARGET_REGISTRY}:${IMAGE_TAG}"
                        sh """
                            docker tag ${IMAGE_NAME}:${IMAGE_TAG} ${TARGET_REGISTRY}:${IMAGE_TAG}
                            docker push ${TARGET_REGISTRY}:${IMAGE_TAG}
                        """
                    }
                }
            }
        }
    }

    post  {
        always {
            echo "Cleaning up local Docker images.."
            sh "docker rmi \$(docker images -q ${IMAGE_NAME}) || true"
        }   
        success {
            echo "Pipeline completed successfully"
        }
        failure {
            echo "Pipeline failed"
        }


    }















}