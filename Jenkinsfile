pipeline {

    agent {
        label 'built-in'
    }

    environment {

        // Maven
        MAVEN_OPTS = '-Xms256m -Xmx768m'

        // SonarQube
        SONAR_PROJECT_KEY = 'spring-petclinic'
        SONAR_PROJECT_NAME = 'Spring PetClinic'
        SONAR_PLUGIN_VERSION = '5.5.0.6356'

        // Application artifact
        ARTIFACT_PATH = 'target/spring-petclinic-4.0.0-SNAPSHOT.jar'

        // Docker/Testcontainers
        DOCKER_HOST = 'unix:///var/run/docker.sock'
    }

    options {

        timestamps()

        disableConcurrentBuilds()

        buildDiscarder(
            logRotator(
                numToKeepStr: '5',
                artifactNumToKeepStr: '5'
            )
        )

        timeout(
            time: 60,
            unit: 'MINUTES'
        )
    }

    stages {

        stage('Environment') {

            steps {

                sh '''
                    set -e

                    echo "=========================================="
                    echo "        ENVIRONMENT INFORMATION"
                    echo "=========================================="

                    echo ""
                    echo "===== SYSTEM ====="
                    uname -a

                    echo ""
                    echo "===== CPU ====="
                    nproc

                    echo ""
                    echo "===== MEMORY ====="
                    free -h

                    echo ""
                    echo "===== DISK ====="
                    df -h .

                    echo ""
                    echo "===== JAVA ====="
                    java -version

                    echo ""
                    echo "===== MAVEN ====="
                    mvn -version

                    echo ""
                    echo "===== DOCKER ====="

                    if command -v docker >/dev/null 2>&1; then
                        docker version
                    else
                        echo "WARNING: Docker CLI not found."
                    fi

                    echo ""
                    echo "===== DOCKER SOCKET ====="

                    if [ -S /var/run/docker.sock ]; then
                        ls -l /var/run/docker.sock
                    else
                        echo "WARNING: /var/run/docker.sock is not available."
                    fi

                    echo ""
                    echo "===== SONAR TOKEN ====="
                    if [ -n "$SONAR_TOKEN" ]; then
                        echo "SONAR_TOKEN is available."
                    else
                        echo "ERROR: SONAR_TOKEN is not available."
                        exit 1
                    fi

                    echo "=========================================="
                '''
            }
        }

        stage('Clean') {

            steps {

                sh '''
                    set -e

                    echo "=========================================="
                    echo "             MAVEN CLEAN"
                    echo "=========================================="

                    mvn -B clean

                    echo "Maven clean completed."
                '''
            }
        }

        stage('Compile') {

            steps {

                sh '''
                    set -e

                    echo "=========================================="
                    echo "             MAVEN COMPILE"
                    echo "=========================================="

                    mvn -B compile

                    echo "Compilation completed successfully."
                '''
            }
        }

        stage('Test') {

            steps {

                sh '''
                    set -e

                    echo "=========================================="
                    echo "              RUNNING TESTS"
                    echo "=========================================="

                    mvn -B test

                    echo "=========================================="
                    echo "              TESTS PASSED"
                    echo "=========================================="
                '''
            }

            post {

                always {

                    junit(
                        allowEmptyResults: true,
                        testResults: 'target/surefire-reports/*.xml'
                    )
                }
            }
        }

        stage('SonarQube Analysis') {

            steps {

                timeout(
                    time: 10,
                    unit: 'MINUTES'
                ) {

                    echo "=========================================="
                    echo "          SONARQUBE ANALYSIS"
                    echo "=========================================="

                    withSonarQubeEnv('SonarQube') {

                        sh '''
                            set -e

                            echo "Starting SonarQube analysis..."

                            mvn -B \
                              org.sonarsource.scanner.maven:sonar-maven-plugin:${SONAR_PLUGIN_VERSION}:sonar \
                              -Dsonar.projectKey="${SONAR_PROJECT_KEY}" \
                              -Dsonar.projectName="${SONAR_PROJECT_NAME}" \
                              -Dsonar.token="${SONAR_TOKEN}"

                            echo ""
                            echo "Checking SonarQube report-task.txt..."

                            if [ -f target/sonar/report-task.txt ]; then
                                echo "SonarQube report-task.txt found."
                                cat target/sonar/report-task.txt
                            else
                                echo "ERROR: target/sonar/report-task.txt was not generated."
                                exit 1
                            fi

                            echo "=========================================="
                            echo "       SONARQUBE ANALYSIS COMPLETE"
                            echo "=========================================="
                        '''
                    }
                }
            }
        }

        stage('Quality Gate') {

            steps {

                echo "=========================================="
                echo "       WAITING FOR QUALITY GATE"
                echo "=========================================="

                timeout(
                    time: 5,
                    unit: 'MINUTES'
                ) {

                    waitForQualityGate(
                        abortPipeline: true
                    )
                }

                echo "=========================================="
                echo "      SONARQUBE QUALITY GATE PASSED"
                echo "=========================================="
            }
        }

        stage('Package') {

            steps {

                sh '''
                    set -e

                    echo "=========================================="
                    echo "              PACKAGING JAR"
                    echo "=========================================="

                    mvn -B package -DskipTests

                    echo "=========================================="
                    echo "              PACKAGE COMPLETE"
                    echo "=========================================="
                '''
            }
        }

        stage('Verify Artifact') {

            steps {

                sh '''
                    set -e

                    echo "=========================================="
                    echo "           VERIFYING ARTIFACT"
                    echo "=========================================="

                    if [ ! -f "$ARTIFACT_PATH" ]; then
                        echo "ERROR: JAR was not generated."
                        echo "Expected:"
                        echo "$ARTIFACT_PATH"
                        exit 1
                    fi

                    echo ""
                    echo "Artifact generated successfully:"
                    ls -lh "$ARTIFACT_PATH"

                    echo ""
                    echo "Absolute path:"
                    realpath "$ARTIFACT_PATH"

                    echo "=========================================="
                    echo "             ARTIFACT READY"
                    echo "=========================================="
                '''
            }
        }

        stage('Archive Artifact') {

            steps {

                archiveArtifacts(
                    artifacts: "${ARTIFACT_PATH}",
                    fingerprint: true,
                    onlyIfSuccessful: true
                )

                echo "JAR archived successfully."
            }
        }
    }

    post {

        success {

            echo '''
==========================================
          PIPELINE SUCCESSFUL
==========================================
Tests: PASSED
SonarQube Analysis: PASSED
Quality Gate: PASSED
Application JAR: BUILT
Artifact: ARCHIVED
==========================================
'''
        }

        failure {

            echo '''
==========================================
            PIPELINE FAILED
==========================================
Check the stage that failed.

Possible blockers:
1. Maven compilation
2. Unit/integration tests
3. Testcontainers Docker access
4. SonarQube analysis
5. SonarQube Quality Gate
6. Packaging
7. Artifact verification
==========================================
'''
        }

        always {

            echo "Pipeline completed: BUILD ${env.BUILD_NUMBER}"

            sh '''
                echo ""
                echo "=========================================="
                echo "        FINAL RESOURCE STATUS"
                echo "=========================================="

                echo ""
                echo "===== DISK ====="
                df -h .

                echo ""
                echo "===== MEMORY ====="
                free -h

                echo ""
                echo "=========================================="
            '''
        }
    }
}
