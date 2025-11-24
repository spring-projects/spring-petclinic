pipeline {

    agent any

    triggers {
        pollSCM('* * * * *')  
    }

    environment {
        MAVEN_OPTS = '-Xmx2048m'
        PROJECT_NAME = 'spring-petclinic'
        SONAR_PROJECT_KEY = 'spring-petclinic'
        DOCKER_ARGS = '-v /var/run/docker.sock:/var/run/docker.sock --network spring-petclinic_devops-net --memory=4g'
    }

    stages {

        /*********************************************
         * Checkout code
         *********************************************/
        stage('Checkout') {
            steps {
                echo 'Checking out code...'
                checkout scm
                script {
                    sh 'git rev-parse --short HEAD'
                    sh 'git log -1 --pretty=%B'
                }
            }
        }

        /*********************************************
         * Build using Java 25
         *********************************************/
        stage('Build (Java 25)') {
            agent {
                docker {
                    image 'maven-java25:latest'
                    args "${DOCKER_ARGS}"
                }
            }
            steps {
                echo 'Building project with Java 25...'
                sh 'chmod +x mvnw'
                sh './mvnw clean compile -DskipTests'
            }
        }


        /*********************************************
         * Unit Tests
         *********************************************/
        stage('Test (Java 25)') {
            agent {
                docker {
                    image 'maven-java25:latest'
                    args "${DOCKER_ARGS}"
                }
            }
            steps {
                echo 'Running unit tests...'
                sh './mvnw test -Dtest="!PostgresIntegrationTests"'
            }
            post {
                always {
                    junit testResults: '**/target/surefire-reports/*.xml', allowEmptyResults: true
                    jacoco(
                        execPattern: '**/target/jacoco.exec',
                        classPattern: '**/target/classes',
                        sourcePattern: '**/src/main/java',
                        exclusionPattern: '**/*Test*.class'
                    )
                }
            }
        }


        /*********************************************
         * SonarQube Analysis
         *********************************************/
        stage('SonarQube Analysis (Java 25)') {
            agent {
                docker {
                    image 'maven-java25:latest'
                    args "${DOCKER_ARGS}"
                }
            }
            steps {
                echo 'Running SonarQube analysis...'
                withSonarQubeEnv('SonarQubeServer') {
                    sh """
                        ./mvnw sonar:sonar \
                        -Dsonar.projectKey=${SONAR_PROJECT_KEY} \
                        -Dsonar.projectName=${PROJECT_NAME} \
                        -Dsonar.projectVersion=${BUILD_NUMBER}
                    """
                }
            }
        }


        /*********************************************
         * Wait for Quality Gate
         *********************************************/
        stage('Quality Gate') {
            steps {
                echo 'Waiting for SonarQube quality gate result...'
                timeout(time: 5, unit: 'MINUTES') {
                    script {
                        def qg = waitForQualityGate abortPipeline: true
                        echo "Quality gate status: ${qg.status}"
                    }
                }
            }
        }


        /*********************************************
         * Package JAR
         *********************************************/
        stage('Package (Java 25)') {
            agent {
                docker {
                    image 'maven-java25:latest'
                    args "${DOCKER_ARGS}"
                }
            }
            steps {
                echo 'Packaging application...'
                sh './mvnw package -DskipTests'
            }
            post {
                success {
                    stash name: 'jar-artifacts', includes: 'target/*.jar', allowEmpty: false
                }
            }
        }

        /*********************************************
         * Archive artifacts
         *********************************************/
        stage('Archive') {
            steps {
                echo 'Archiving artifacts...'
                unstash 'jar-artifacts'
                archiveArtifacts artifacts: 'target/*.jar',
                    fingerprint: true,
                    allowEmptyArchive: false
            }
        }


        /*********************************************
         * OWASP ZAP Scan
         *********************************************/
        stage('OWASP ZAP Scan') {
            steps {
                echo 'Running OWASP ZAP Baseline Scan...'
                sh '''
                set -e
                ZAP_IMAGE="owasp/zap2docker-stable"
                ZAP_FALLBACK_IMAGE="ghcr.io/zaproxy/zaproxy:stable"

                echo "Pulling ZAP image: ${ZAP_IMAGE}"
                if ! docker pull "${ZAP_IMAGE}"; then
                    echo "Primary pull failed, trying fallback: ${ZAP_FALLBACK_IMAGE}"
                    docker pull "${ZAP_FALLBACK_IMAGE}"
                    ZAP_IMAGE="${ZAP_FALLBACK_IMAGE}"
                fi

                docker run --rm \
                    --network=spring-petclinic_devops-net \
                    -v $(pwd):/zap/wrk \
                    --user=$(id -u):$(id -g) \
                    "${ZAP_IMAGE}" zap-baseline.py \
                    -t http://petclinic:8080 \
                    -r zap_report.html \
                    -I

                if [ -f zap_report.html ]; then
                    echo "ZAP report generated at $(pwd)/zap_report.html"
                else
                    echo "ZAP report not found; creating placeholder for visibility"
                    cat > zap_report.html <<'EOF'
<html>
  <body>
    <h1>OWASP ZAP report missing</h1>
    <p>The ZAP container did not produce zap_report.html. Check ZAP stage logs for details.</p>
  </body>
</html>
EOF
                fi
                '''
            }
        }


        /*********************************************
         * Publish ZAP HTML Report
         *********************************************/
        stage('Publish ZAP Report') {
            steps {
                echo 'Publishing OWASP ZAP report...'
                publishHTML target: [
                    allowMissing: true,
                    reportDir: '.',
                    reportFiles: 'zap_report.html',
                    reportName: 'OWASP ZAP Security Report'
                ]
            }
        }
    }


    post {
        success {
            echo 'Build successful!'
        }
        failure {
            echo 'Build failed!'
        }
    }
}
