pipeline {

    agent any

    environment {
        SONARQUBE = 'SonarQube'
    }

    stages {

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
                echo 'Building Spring PetClinic and running tests...'

                sh '''
                    mvn clean verify
                '''
            }
        }

        stage('SonarQube Analysis') {
            steps {
                echo 'Running SonarQube analysis...'

                withSonarQubeEnv("${SONARQUBE}") {
                    sh '''
                        mvn sonar:sonar \
                          -Dsonar.projectKey=spring-petclinic \
                          -Dsonar.projectName=Spring-Petclinic
                    '''
                }
            }
        }

        stage('Quality Gate') {
            steps {
                echo 'Waiting for SonarQube Quality Gate...'

                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('Package') {
            steps {
                echo 'Creating application package...'

                sh '''
                    mvn package -DskipTests
                '''
            }
        }

        stage('Archive Artifact') {
            steps {
                echo 'Archiving build artifacts...'

                archiveArtifacts artifacts: 'target/*.jar',
                                 fingerprint: true
            }
        }
    }

    post {
        success {
            echo '''
========================================
PIPELINE SUCCESS
========================================
Spring PetClinic build completed successfully.
Artifact has been archived.
========================================
'''
        }

        failure {
            echo '''
========================================
PIPELINE FAILED
========================================
Check the failed stage in Jenkins.
========================================
'''
        }

        always {
            echo 'Pipeline execution completed.'
        }
    }
}
