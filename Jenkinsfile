pipeline {

    agent any

    triggers {
        pollSCM('* * * * *')  
    }

    environment {
        MAVEN_OPTS = '-Xmx1024m'
        PROJECT_NAME = 'spring-petclinic'
        SONAR_PROJECT_KEY = 'spring-petclinic'
        DOCKER_ARGS = '-v /var/run/docker.sock:/var/run/docker.sock'
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
                    image 'maven:3.9.5-eclipse-temurin-25'
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
                    image 'maven:3.9.5-eclipse-temurin-25'
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
                    image 'maven:3.9.5-eclipse-temurin-25'
                    args "${DOCKER_ARGS}"
                }
            }
            steps {
                echo 'Running SonarQube analysis...'
                withSonarQubeEnv('SonarQubeServer') {
                    sh """
                        ./mvnw clean verify sonar:sonar \
                        -DskipTests \
                        -Dsonar.projectKey=${SONAR_PROJECT_KEY} \
                        -Dsonar.projectName=${PROJECT_NAME} \
                        -Dsonar.projectVersion=${BUILD_NUMBER} \
                        -Dsonar.host.url=http://sonarqube:9000 \
                        -Dsonar.token=${SONAR_AUTH_TOKEN}
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
                timeout(time: 10, unit: 'MINUTES') {
                    script {
                        def qg = waitForQualityGate abortPipeline: true
                        echo "Quality gate status: ${qg.status}"
                    }
                }
            }
        }


        /*********************************************
         * Checkstyle
         *********************************************/
        stage('Checkstyle (Java 25)') {
            agent {
                docker {
                    image 'maven:3.9.5-eclipse-temurin-25'
                    args "${DOCKER_ARGS}"
                }
            }
            steps {
                echo 'Running Checkstyle analysis...'
                sh './mvnw checkstyle:checkstyle'
            }
            post {
                always {
                    recordIssues(
                        enabledForFailure: true,
                        tool: checkStyle(pattern: '**/target/checkstyle-result.xml')
                    )
                }
            }
        }


        /*********************************************
         * Package JAR
         *********************************************/
        stage('Package (Java 25)') {
            agent {
                docker {
                    image 'maven:3.9.5-eclipse-temurin-25'
                    args "${DOCKER_ARGS}"
                }
            }
            steps {
                echo 'Packaging application...'
                sh './mvnw package -DskipTests'
            }
        }

        /*********************************************
         * Archive artifacts
         *********************************************/
        stage('Archive') {
            steps {
                echo 'Archiving artifacts...'
                archiveArtifacts artifacts: '**/target/*.jar',
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
                sh """
                docker run --rm \
                    --network=spring-petclinic_devops-net \
                    -v \$(pwd):/zap/wrk \
                    owasp/zap2docker-stable zap-baseline.py \
                    -t http://petclinic:8080 \
                    -r zap_report.html \
                    -I
                """
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
