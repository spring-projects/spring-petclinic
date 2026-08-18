pipeline {

    agent any

    environment {
        SONARQUBE_SERVER = 'SonarQube'
    }

    stages {

        stage('Checkout') {
            steps {
                echo 'Checking out source code...'

                git branch: 'devops-project',
                    credentialsId: 'git-cred',
                    url: 'https://github.com/HarshithaLYadav/spring-petclinic.git'
            }
        }

        stage('Environment') {
            steps {
                echo 'Checking build environment...'

                sh '''
                    echo "Java version:"
                    java -version

                    echo "Maven version:"
                    mvn -version

                    echo "Git version:"
                    git --version
                '''
            }
        }

        stage('Build & Test') {
            steps {
                echo 'Building application and running tests...'

                sh '''
                    ./mvnw clean verify
                '''
            }

            post {
                always {
                    junit testResults: '**/target/surefire-reports/*.xml',
                          allowEmptyResults: true
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                echo 'Running SonarQube analysis...'

                withSonarQubeEnv("${SONARQUBE_SERVER}") {
                    sh '''
                        ./mvnw sonar:sonar \
                          -Dsonar.projectKey=Spring-Petclinic \
                          -Dsonar.projectName=Spring-Petclinic
                    '''
                }
            }
        }

        stage('Quality Gate') {
            steps {
                echo 'Waiting for SonarQube Quality Gate...'

                timeout(time: 10, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('Package') {
            steps {
                echo 'Packaging application...'

                sh '''
                    ./mvnw package -DskipTests
                '''
            }
        }

        stage('Archive Artifact') {
            steps {
                echo 'Archiving JAR artifact...'

                archiveArtifacts artifacts: 'target/*.jar',
                                 fingerprint: true
            }
        }
    }

    post {

        success {
            echo '========================================'
            echo 'PIPELINE SUCCESSFUL'
            echo 'Build, Tests, SonarQube and Packaging passed.'
            echo '========================================'
        }

        failure {
            echo '========================================'
            echo 'PIPELINE FAILED'
            echo 'Check the failed stage in Jenkins.'
            echo '========================================'
        }

        always {
            echo "Build completed: ${currentBuild.currentResult}"
        }
    }
}
