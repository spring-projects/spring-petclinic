pipeline {

    agent {
        label 'built-in'
    }

    options {
        skipDefaultCheckout(true)
        disableConcurrentBuilds()
        timestamps()

        buildDiscarder(
            logRotator(
                numToKeepStr: '10',
                artifactNumToKeepStr: '5'
            )
        )

        timeout(time: 30, unit: 'MINUTES')
    }

    environment {
        MAVEN_OPTS = '-Xms256m -Xmx768m'
        MAVEN_USER_HOME = '/var/jenkins_home/.m2'

        SONARQUBE_SERVER = 'SonarQube'
        SONAR_PROJECT_KEY = 'spring-petclinic'

        APP_VERSION = '4.0.0-SNAPSHOT'
    }

    stages {

        stage('Checkout') {
            steps {
                echo '===== CHECKOUT ====='

                checkout([
                    $class: 'GitSCM',
                    branches: [[name: '*/devops-project']],
                    userRemoteConfigs: [[
                        url: 'https://github.com/HarshithaLYadav/spring-petclinic.git',
                        credentialsId: 'git-token'
                    ]]
                ])

                sh '''
                    echo "Branch:"
                    git branch --show-current || true

                    echo "Commit:"
                    git log -1 --oneline
                '''
            }
        }

        stage('Environment') {
            steps {
                echo '===== ENVIRONMENT ====='

                sh '''
                    set -e

                    echo "Java:"
                    java -version

                    echo ""
                    echo "Maven:"
                    mvn -version

                    echo ""
                    echo "Git:"
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
                echo '===== BUILD & TEST ====='

                sh '''
                    set -e

                    export MAVEN_OPTS="-Xms256m -Xmx768m"

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
                echo '===== SONARQUBE ANALYSIS ====='

                withSonarQubeEnv("${SONARQUBE_SERVER}") {

                    withCredentials([
                        string(
                            credentialsId: 'sonar-token',
                            variable: 'SONAR_TOKEN'
                        )
                    ]) {

                        sh '''
                            set -e

                            export MAVEN_OPTS="-Xms256m -Xmx768m"

                            mvn -B sonar:sonar \
                                -Dsonar.projectKey="${SONAR_PROJECT_KEY}" \
                                -Dsonar.projectName="Spring PetClinic" \
                                -Dsonar.token="${SONAR_TOKEN}"
                        '''
                    }
                }
            }
        }

        stage('Quality Gate') {
            options {
                timeout(time: 10, unit: 'MINUTES')
            }

            steps {
                echo '===== QUALITY GATE ====='

                waitForQualityGate(
                    abortPipeline: true
                )
            }
        }

        stage('Package') {
            steps {
                echo '===== PACKAGE ====='

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
                echo '===== ARCHIVE ARTIFACT ====='

                archiveArtifacts(
                    artifacts: 'target/*.jar',
                    fingerprint: true,
                    allowEmptyArchive: false
                )
            }
        }
    }

    post {

        success {
            echo '''
========================================
       PIPELINE SUCCESS
========================================
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
