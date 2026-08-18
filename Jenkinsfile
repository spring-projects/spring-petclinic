pipeline {

    agent {
        label 'built-in'
    }

    options {
        skipDefaultCheckout(true)

        // Prevent a permanently stuck build
        timeout(time: 30, unit: 'MINUTES')

        // Keep console output manageable
        timestamps()

        // Do not run two builds simultaneously
        disableConcurrentBuilds()

        // Keep only the last 10 builds
        buildDiscarder(
            logRotator(
                numToKeepStr: '10',
                artifactNumToKeepStr: '5'
            )
        )
    }

    environment {

        // Maven configuration for 4 GB VM
        MAVEN_OPTS = '-Xms256m -Xmx768m'

        // SonarQube server configured in Jenkins
        SONARQUBE_SERVER = 'SonarQube'

        // Sonar project
        SONAR_PROJECT_KEY = 'spring-petclinic'

        // Maven local repository inside Jenkins home
        MAVEN_USER_HOME = '/var/jenkins_home/.m2'

        // Application version
        APP_VERSION = '4.0.0-SNAPSHOT'

        // Generated JAR
        JAR_FILE = 'target/petclinic-4.0.0-SNAPSHOT.jar'
    }

    stages {

        /*
         * ==========================================
         * 1. CHECKOUT
         * ==========================================
         */
        stage('Checkout') {
            steps {

                echo '========================================'
                echo 'CHECKOUT SOURCE CODE'
                echo '========================================'

                checkout scm

                sh '''
                    echo "Current branch:"
                    git branch --show-current || true

                    echo ""
                    echo "Current commit:"
                    git log -1 --oneline

                    echo ""
                    echo "Git version:"
                    git --version
                '''
            }
        }


        /*
         * ==========================================
         * 2. ENVIRONMENT
         * ==========================================
         */
        stage('Environment') {
            steps {

                echo '========================================'
                echo 'BUILD ENVIRONMENT'
                echo '========================================'

                sh '''
                    echo "Java version:"
                    java -version

                    echo ""
                    echo "Maven version:"
                    mvn -version

                    echo ""
                    echo "Git version:"
                    git --version

                    echo ""
                    echo "Available memory:"
                    free -h || true

                    echo ""
                    echo "Disk space:"
                    df -h .
                '''
            }
        }


        /*
         * ==========================================
         * 3. BUILD & TEST
         * ==========================================
         */
        stage('Build & Test') {

            options {
                timeout(time: 15, unit: 'MINUTES')
            }

            steps {

                echo '========================================'
                echo 'BUILD & TEST'
                echo '========================================'

                sh '''
                    set -e

                    export MAVEN_OPTS="-Xms256m -Xmx768m"

                    echo "MAVEN_OPTS=$MAVEN_OPTS"

                    mvn -B clean verify
                '''
            }

            post {

                always {

                    echo 'Publishing test results...'

                    junit(
                        testResults: 'target/surefire-reports/*.xml',
                        allowEmptyResults: true
                    )

                    archiveArtifacts(
                        artifacts: 'target/surefire-reports/**/*',
                        allowEmptyArchive: true
                    )
                }
            }
        }


        /*
         * ==========================================
         * 4. SONARQUBE ANALYSIS
         * ==========================================
         */
        stage('SonarQube Analysis') {

            options {
                timeout(time: 10, unit: 'MINUTES')
            }

            steps {

                echo '========================================'
                echo 'SONARQUBE ANALYSIS'
                echo '========================================'

                withSonarQubeEnv("${SONARQUBE_SERVER}") {

                    sh '''
                        set -e

                        export MAVEN_OPTS="-Xms256m -Xmx768m"

                        echo "Running SonarQube analysis..."

                        mvn -B sonar:sonar \
                            -Dsonar.projectKey="${SONAR_PROJECT_KEY}" \
                            -Dsonar.projectName="Spring PetClinic" \
                            -Dsonar.host.url="${SONAR_HOST_URL}"
                    '''
                }
            }
        }


        /*
         * ==========================================
         * 5. QUALITY GATE
         * ==========================================
         */
        stage('Quality Gate') {

            options {
                timeout(time: 10, unit: 'MINUTES')
            }

            steps {

                echo '========================================'
                echo 'SONARQUBE QUALITY GATE'
                echo '========================================'

                waitForQualityGate(
                    abortPipeline: true
                )
            }
        }


        /*
         * ==========================================
         * 6. PACKAGE
         * ==========================================
         */
        stage('Package') {

            steps {

                echo '========================================'
                echo 'PACKAGE APPLICATION'
                echo '========================================'

                sh '''
                    set -e

                    export MAVEN_OPTS="-Xms256m -Xmx768m"

                    mvn -B package -DskipTests

                    echo ""
                    echo "Generated artifacts:"
                    ls -lh target/*.jar
                '''
            }
        }


        /*
         * ==========================================
         * 7. ARCHIVE ARTIFACT
         * ==========================================
         */
        stage('Archive Artifact') {

            steps {

                echo '========================================'
                echo 'ARCHIVE JAR'
                echo '========================================'

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
     * ==========================================
     * POST ACTIONS
     * ==========================================
     */
    post {

        success {

            echo '''
========================================
PIPELINE SUCCESS
========================================

Spring PetClinic pipeline completed successfully.

Stages completed:
1. Checkout
2. Environment
3. Build & Test
4. SonarQube Analysis
5. Quality Gate
6. Package
7. Archive Artifact

JAR artifact is available in Jenkins.
========================================
'''
        }

        failure {

            echo '''
========================================
PIPELINE FAILED
========================================

Check the failed stage above.

Possible areas:
- Maven build
- Unit tests
- SonarQube
- Quality Gate
- Packaging
- Jenkins resources
========================================
'''
        }

        always {

            echo 'Pipeline execution completed.'

            sh '''
                echo ""
                echo "Final disk usage:"
                df -h . || true

                echo ""
                echo "Final memory:"
                free -h || true
            '''
        }
    }
}
