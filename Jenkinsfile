pipeline {

    agent {
        label 'built-in'
    }

    options {
        timestamps()
        disableConcurrentBuilds()
        timeout(time: 60, unit: 'MINUTES')
        skipDefaultCheckout(true)
    }

    environment {
        // SonarQube
        SONAR_PROJECT_KEY = 'spring-petclinic'
        SONAR_HOST_URL    = 'http://host.docker.internal:9000'

        // Docker
        DOCKER_IMAGE = 'spring-petclinic:4.0.0'
        CONTAINER_NAME = 'spring-petclinic'
        APP_PORT = '8081'

        // Azure Storage
        AZURE_STORAGE_ACCOUNT = 'chatstreamapk8055'
        AZURE_CONTAINER = 'stremchat-cont'
    }

    stages {

        /*
         * ============================================================
         * 1. CLEAN WORKSPACE
         * ============================================================
         */
        stage('Clean Workspace') {
            steps {
                deleteDir()

                sh '''
                    echo "======================================"
                    echo " CLEAN WORKSPACE"
                    echo "======================================"

                    pwd
                    df -h .
                '''
            }
        }


        /*
         * ============================================================
         * 2. CHECKOUT
         * ============================================================
         */
        stage('Checkout') {
            steps {
                checkout scm

                sh '''
                    echo "======================================"
                    echo " SOURCE CODE"
                    echo "======================================"

                    git branch --show-current || true
                    git log -1 --oneline

                    echo ""
                    echo "Repository files:"
                    ls -la
                '''
            }
        }


        /*
         * ============================================================
         * 3. VERIFY TOOLS
         * ============================================================
         */
        stage('Verify Tools') {
            steps {
                sh '''
                    set -e

                    echo "======================================"
                    echo " VERIFYING JENKINS TOOLS"
                    echo "======================================"

                    echo ""
                    echo "=== JAVA ==="
                    java -version

                    echo ""
                    echo "=== GIT ==="
                    git --version

                    echo ""
                    echo "=== MAVEN ==="
                    ./mvnw -version

                    echo ""
                    echo "=== TERRAFORM ==="
                    terraform version

                    echo ""
                    echo "=== AZURE CLI ==="
                    az version --output table

                    echo ""
                    echo "=== DOCKER ==="
                    docker --version

                    echo ""
                    echo "=== DOCKER ACCESS ==="
                    docker ps

                    echo ""
                    echo "All required tools are available."
                '''
            }
        }


        /*
         * ============================================================
         * 4. BUILD + UNIT TESTS + JACOCO
         * ============================================================
         */
        stage('Build & Test') {
            steps {
                sh '''
                    set -e

                    echo "======================================"
                    echo " MAVEN BUILD + TESTS"
                    echo "======================================"

                    chmod +x mvnw

                    ./mvnw clean verify \
                        -DskipTests=false \
                        -Dmaven.test.failure.ignore=false

                    echo ""
                    echo "Build completed successfully."

                    echo ""
                    echo "Checking JaCoCo report..."

                    if [ -f target/site/jacoco/jacoco.xml ]; then
                        ls -lh target/site/jacoco/jacoco.xml
                    else
                        echo "WARNING: JaCoCo XML report was not generated."
                    fi
                '''
            }

            post {
                always {
                    junit(
                        testResults: 'target/surefire-reports/*.xml',
                        allowEmptyResults: true
                    )

                    archiveArtifacts(
                        artifacts: 'target/*.jar',
                        allowEmptyArchive: true
                    )
                }
            }
        }


        /*
         * ============================================================
         * 5. SONARQUBE
         * ============================================================
         */
        stage('SonarQube Analysis') {
            steps {
                withCredentials([
                    string(
                        credentialsId: 'sonar-token',
                        variable: 'SONAR_TOKEN'
                    )
                ]) {
                    sh '''
                        set -e

                        echo "======================================"
                        echo " SONARQUBE ANALYSIS"
                        echo "======================================"

                        ./mvnw sonar:sonar \
                            -Dsonar.projectKey="${SONAR_PROJECT_KEY}" \
                            -Dsonar.host.url="${SONAR_HOST_URL}" \
                            -Dsonar.token="${SONAR_TOKEN}"
                    '''
                }
            }
        }


        /*
         * ============================================================
         * 6. DOCKER BUILD
         * ============================================================
         */
        stage('Docker Build') {
            steps {
                sh '''
                    set -e

                    echo "======================================"
                    echo " DOCKER BUILD"
                    echo "======================================"

                    docker version

                    echo ""
                    echo "Removing old image if present..."

                    docker image rm "${DOCKER_IMAGE}" 2>/dev/null || true

                    echo ""
                    echo "Building image..."

                    docker build \
                        -t "${DOCKER_IMAGE}" \
                        .

                    echo ""
                    echo "Docker image created:"
                    docker images "${DOCKER_IMAGE}"
                '''
            }
        }


        /*
         * ============================================================
         * 7. DEPLOY CONTAINER
         * ============================================================
         */
        stage('Deploy Container') {
            steps {
                sh '''
                    set -e

                    echo "======================================"
                    echo " DEPLOY SPRING PETCLINIC"
                    echo "======================================"

                    echo "Stopping old container if it exists..."

                    docker rm -f "${CONTAINER_NAME}" 2>/dev/null || true

                    echo ""
                    echo "Starting new container..."

                    docker run -d \
                        --name "${CONTAINER_NAME}" \
                        -p "${APP_PORT}:8080" \
                        "${DOCKER_IMAGE}"

                    echo ""
                    echo "Container started."

                    docker ps \
                        --filter "name=${CONTAINER_NAME}"

                    echo ""
                    echo "Waiting for application..."

                    sleep 20

                    echo ""
                    echo "Container logs:"

                    docker logs --tail 50 "${CONTAINER_NAME}"
                '''
            }
        }


        /*
         * ============================================================
         * 8. E2E TEST
         * ============================================================
         */
        stage('E2E Tests') {
            steps {
                sh '''
                    set -e

                    echo "======================================"
                    echo " END-TO-END TESTS"
                    echo "======================================"

                    chmod +x e2e-test.sh

                    ./e2e-test.sh
                '''
            }
        }


        /*
         * ============================================================
         * 9. TERRAFORM VALIDATION
         * ============================================================
         */
        stage('Terraform Validate') {
            steps {
                dir('terraform') {
                    sh '''
                        set -e

                        echo "======================================"
                        echo " TERRAFORM VALIDATION"
                        echo "======================================"

                        terraform version

                        terraform init -input=false

                        terraform validate

                        echo ""
                        echo "Terraform validation successful."
                    '''
                }
            }
        }


        /*
         * ============================================================
         * 10. DOCKER VERIFICATION
         * ============================================================
         */
        stage('Final Verification') {
            steps {
                sh '''
                    echo "======================================"
                    echo " FINAL VERIFICATION"
                    echo "======================================"

                    echo ""
                    echo "=== CONTAINERS ==="
                    docker ps

                    echo ""
                    echo "=== IMAGES ==="
                    docker images | head -20

                    echo ""
                    echo "=== APPLICATION ==="

                    if curl -fsS "http://localhost:${APP_PORT}/" >/dev/null; then
                        echo "Spring Petclinic is UP."
                    else
                        echo "WARNING: Application health check failed."
                    fi

                    echo ""
                    echo "======================================"
                    echo " PIPELINE VERIFICATION COMPLETED"
                    echo "======================================"
                '''
            }
        }
    }


    /*
     * ================================================================
     * POST ACTIONS
     * ================================================================
     */
    post {

        success {
            echo '''
========================================
 Jenkins Pipeline SUCCESS
========================================
Build: ${BUILD_NUMBER}
Job:   ${JOB_NAME}

Spring Petclinic:
Docker build       : SUCCESS
Application deploy : SUCCESS
E2E tests          : SUCCESS
Terraform validate : SUCCESS
SonarQube          : COMPLETED
========================================
'''
        }

        failure {
            echo '''
========================================
 Jenkins Pipeline FAILED
========================================
Build: ${BUILD_NUMBER}
Job:   ${JOB_NAME}

Check the failed stage above.
========================================
'''

            sh '''
                echo "=== FAILED BUILD DEBUG ==="

                echo ""
                echo "Docker containers:"
                docker ps -a || true

                echo ""
                echo "Spring Petclinic logs:"
                docker logs --tail 100 spring-petclinic 2>/dev/null || true
            '''
        }

        always {
            echo "Cleaning temporary workspace files..."

            sh '''
                echo "Pipeline completed."
                df -h .
            '''
        }
    }
}
