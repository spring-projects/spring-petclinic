pipeline {

    agent {
        label 'built-in'
    }

    options {
        skipDefaultCheckout(true)

        timeout(
            time: 30,
            unit: 'MINUTES'
        )

        timestamps()

        disableConcurrentBuilds()

        buildDiscarder(
            logRotator(
                numToKeepStr: '10',
                artifactNumToKeepStr: '5'
            )
        )
    }

    environment {

        // Maven memory - important because VM has 4 GB RAM
        MAVEN_OPTS = '-Xms256m -Xmx768m'

        // Maven local repository
        MAVEN_USER_HOME = '/var/jenkins_home/.m2'

        // SonarQube Jenkins server configuration
        SONARQUBE_SERVER = 'SonarQube'

        // SonarQube project
        SONAR_PROJECT_KEY = 'spring-petclinic'

        SONAR_PROJECT_NAME = 'Spring PetClinic'

        // Application version
        APP_VERSION = '4.0.0-SNAPSHOT'

        // Generated JAR
        JAR_FILE = 'target/petclinic-4.0.0-SNAPSHOT.jar'
    }

    stages {

        /*
         * ============================================================
         * 1. CHECKOUT
         * ============================================================
         */

        stage('Checkout') {

            steps {

                echo '''
========================================
CHECKOUT SOURCE CODE
========================================
'''

                // Jenkins automatically uses the SCM configuration
                // and the configured git-token credential.
                checkout scm

                sh '''
                    echo "Branch:"
                    git branch --show-current || true

                    echo ""
                    echo "Commit:"
                    git log -1 --oneline

                    echo ""
                    echo "Git:"
                    git --version
                '''
            }
        }


        /*
         * ============================================================
         * 2. ENVIRONMENT
         * ============================================================
         */

        stage('Environment') {

            steps {

                echo '''
========================================
BUILD ENVIRONMENT
========================================
'''

                sh '''
                    echo "JAVA:"
                    java -version

                    echo ""
                    echo "MAVEN:"
                    mvn -version

                    echo ""
                    echo "GIT:"
                    git --version

                    echo ""
                    echo "MEMORY:"
                    free -h

                    echo ""
                    echo "DISK:"
                    df -h .
                '''
            }
        }


        /*
         * ============================================================
         * 3. BUILD AND TEST
         * ============================================================
         */

        stage('Build & Test') {

            options {

                timeout(
                    time: 15,
                    unit: 'MINUTES'
                )
            }

            steps {

                echo '''
========================================
BUILD & TEST
========================================
'''

                sh '''
                    set -e

                    export MAVEN_OPTS="-Xms256m -Xmx768m"

                    echo "MAVEN_OPTS=$MAVEN_OPTS"

                    echo ""
                    echo "Starting Maven build..."

                    mvn -B clean verify
                '''
            }

            post {

                always {

                    echo "Publishing JUnit test results..."

                    junit(
                        testResults: 'target/surefire-reports/*.xml',
                        allowEmptyResults: true
                    )
                }
            }
        }


        /*
         * ============================================================
         * 4. SONARQUBE ANALYSIS
         * ============================================================
         */

        stage('SonarQube Analysis') {

            options {

                timeout(
                    time: 10,
                    unit: 'MINUTES'
                )
            }

            steps {

                echo '''
========================================
SONARQUBE ANALYSIS
========================================
'''

                withSonarQubeEnv("${SONARQUBE_SERVER}") {

                    sh '''
                        set -e

                        export MAVEN_OPTS="-Xms256m -Xmx768m"

                        echo "SonarQube URL:"
                        echo "$SONAR_HOST_URL"

                        echo ""
                        echo "Running SonarQube analysis..."

                        mvn -B sonar:sonar \
                            -Dsonar.projectKey="${SONAR_PROJECT_KEY}" \
                            -Dsonar.projectName="${SONAR_PROJECT_NAME}" \
                            -Dsonar.host.url="${SONAR_HOST_URL}"
                    '''
                }
            }
        }


        /*
         * ============================================================
         * 5. QUALITY GATE
         * ============================================================
         */

        stage('Quality Gate') {

            options {

                timeout(
                    time: 10,
                    unit: 'MINUTES'
                )
            }

            steps {

                echo '''
========================================
SONARQUBE QUALITY GATE
========================================
'''

                echo "Waiting for SonarQube Quality Gate..."

                waitForQualityGate(
                    abortPipeline: true
                )
            }
        }


        /*
         * ============================================================
         * 6. PACKAGE
         * ============================================================
         */

        stage('Package') {

            steps {

                echo '''
========================================
PACKAGE APPLICATION
========================================
'''

                sh '''
                    set -e

                    export MAVEN_OPTS="-Xms256m -Xmx768m"

                    echo "Packaging application..."

                    mvn -B package -DskipTests

                    echo ""
                    echo "Generated artifacts:"

                    ls -lh target/*.jar
                '''
            }
        }


        /*
         * ============================================================
         * 7. ARCHIVE ARTIFACT
         * ============================================================
         */

        stage('Archive Artifact') {

            steps {

                echo '''
========================================
ARCHIVE ARTIFACT
========================================
'''

                archiveArtifacts(
                    artifacts: 'target/*.jar',
                    fingerprint: true,
                    allowEmptyArchive: false
                )

                echo "JAR archived successfully."
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
PIPELINE SUCCESS
========================================

Spring PetClinic CI Pipeline
completed successfully.

Stages completed:

1. Checkout
2. Environment
3. Build & Test
4. SonarQube Analysis
5. Quality Gate
6. Package
7. Archive Artifact

========================================
'''
        }


        failure {

            echo '''
========================================
PIPELINE FAILED
========================================

One of the pipeline stages failed.

Check the Jenkins console output
above to identify the failed stage.

========================================
'''
        }


        always {

            echo '''
========================================
PIPELINE COMPLETED
========================================
'''

            sh '''
                echo ""
                echo "Final Memory:"
                free -h || true

                echo ""
                echo "Final Swap:"
                swapon --show || true

                echo ""
                echo "Final Disk:"
                df -h . || true
            '''
        }
    }
}
