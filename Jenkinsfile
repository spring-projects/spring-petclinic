pipeline {
    agent any
    
    tools {
        maven 'Maven 3.9.5'
        jdk 'JDK 25'
    }
    
    triggers {
        pollSCM('H/5 * * * *')
    }
    
    environment {
        MAVEN_OPTS = '-Xmx1024m'
        PROJECT_NAME = 'spring-petclinic'
        SONAR_PROJECT_KEY = 'spring-petclinic'
    }
    
    stages {

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
         * All Maven steps must run using Java 25 Docker
         *********************************************/
        stage('Build + Test + Sonar Preparation (Java 25)') {
            agent {
                docker {
                    image 'maven:3.9.5-eclipse-temurin-25'
                    args '-v /var/run/docker.sock:/var/run/docker.sock'
                }
            }
            stages {
                stage('Build') {
                    steps {
                        echo 'Building project...'
                        sh './mvnw clean compile -DskipTests'
                    }
                }

                stage('Test') {
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

                stage('SonarQube Analysis') {
                    steps {
                        echo 'Running SonarQube analysis...'
                        withSonarQubeEnv('SonarQubeServer') {
                            sh """
                                ./mvnw clean verify sonar:sonar \
                                -DskipTests \
                                -Dsonar.projectKey=${env.SONAR_PROJECT_KEY} \
                                -Dsonar.projectName=${env.PROJECT_NAME} \
                                -Dsonar.projectVersion=${env.BUILD_NUMBER} \
                                -Dsonar.host.url=http://sonarqube:9000 \
                                -Dsonar.token=${SONAR_AUTH_TOKEN}
                            """
                        }
                    }
                }

                stage('Quality Gate') {
                    steps {
                        echo 'Waiting for SonarQube quality gate result...'
                        sleep(15)
                        timeout(time: 10, unit: 'MINUTES') {
                            script {
                                def qg = waitForQualityGate abortPipeline: true
                                echo "Quality gate status: ${qg.status}"
                            }
                        }
                    }
                }

                stage('Code Quality') {
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

                stage('Package') {
                    steps {
                        echo 'Packaging application...'
                        sh './mvnw package -DskipTests'
                    }
                }
            }
        }

        /*********************************************
         * Artifact / ZAP Scan (still outside Docker)
         *********************************************/
        stage('Archive') {
            steps {
                echo 'Archiving artifacts...'
                archiveArtifacts artifacts: '**/target/*.jar',
                                    fingerprint: true,
                                    allowEmptyArchive: false
            }
        }
        
        stage('OWASP ZAP Scan') {
            steps {
                echo 'Running OWASP ZAP Baseline Scan...'
                sh """
                docker run --rm -v \$(pwd):/zap/wrk owasp/zap2docker-stable zap-baseline.py \
                    -t http://petclinic:8080 \
                    -r zap_report.html \
                    -I
                """
            }
        }

        stage('Publish ZAP Report') {
            steps {
                echo 'Publishing OWASP ZAP HTML report...'
                publishHTML target: [
                    allowMissing: false,
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
        always {
            echo 'Cleanup after build...'
        }
    }
}
