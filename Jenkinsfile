pipeline {

    agent {
        label 'built-in'
    }

    options {
        skipDefaultCheckout(true)
        timeout(time: 30, unit: 'MINUTES')
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
        MAVEN_OPTS = '-Xms256m -Xmx768m'
        SONARQUBE_SERVER = 'SonarQube'
        SONAR_PROJECT_KEY = 'spring-petclinic'
        MAVEN_USER_HOME = '/var/jenkins_home/.m2'
        APP_VERSION = '4.0.0-SNAPSHOT'
        JAR_FILE = 'target/petclinic-4.0.0-SNAPSHOT.jar'
    }

    stages {

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
                    echo "Memory:"
                    free -h

                    echo ""
                    echo "Disk:"
                    df -h .
                '''
            }
        }

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
                    junit(
                        testResults: 'target/surefire-reports/*.xml',
                        allowEmptyResults: true
                    )
                }
            }
        }

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

                        mvn -B sonar:sonar \
                            -Dsonar.projectKey="${SONAR_PROJECT_KEY}" \
                            -Dsonar.projectName="Spring PetClinic" \
                            -Dsonar.host.url="${SONAR_HOST_URL}"
                    '''
                }
            }
        }

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
                    echo "Generated JAR:"
                    ls -lh target/*.jar
                '''
            }
        }

        stage('Archive Artifact') {
            steps {
                echo '========================================'
                echo 'ARCHIVE ARTIFACT'
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

    post {

        success {
            echo '''
========================================
PIPELINE SUCCESS
========================================

Spring PetClinic pipeline completed successfully.

Checkout
Environment
Build & Test
SonarQube Analysis
Quality Gate
Package
Archive Artifact

========================================
'''
        }

        failure {
            echo '''
========================================
PIPELINE FAILED
========================================

Check the failed stage above.

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
