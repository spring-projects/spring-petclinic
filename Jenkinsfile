pipeline {
    agent any

    triggers {
        // Trigger every 5 minutes, only on Thursdays (4 = Thursday in cron)
        cron('H/5 * * * 4')
    }

    tools {
        maven 'Maven'
        jdk 'JDK25'
    }

    stages {

        stage('Checkout') {
            steps {
                echo 'Checking out source code...'
                checkout scm
            }
        }

        stage('Build') {
            steps {
                echo 'Building the project...'
                bat 'mvn clean compile -DskipTests'
            }
        }

        stage('Test & Code Coverage (JaCoCo)') {
            steps {
                echo 'Running tests and generating JaCoCo coverage report...'
                bat 'mvn test jacoco:report'
            }
            post {
                always {
                    // Publish JUnit test results
                    junit(
                        testResults: '**/target/surefire-reports/*.xml',
                        allowEmptyResults: true
                    )
                    // Publish coverage using the Coverage plugin with JaCoCo parser
                    recordCoverage(
                        tools: [[parser: 'JACOCO', pattern: '**/target/site/jacoco/jacoco.xml']],
                        sourceCodeRetention: 'EVERY_BUILD'
                    )
                }
            }
        }

        stage('Package') {
            steps {
                echo 'Packaging the application artifact...'
                bat 'mvn package -DskipTests'
            }
            post {
                success {
                    // Archive the generated JAR artifact
                    archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                }
            }
        }

    }

    post {
        always {
            echo 'Pipeline execution complete.'
        }
        success {
            echo 'Build succeeded!'
        }
        failure {
            echo 'Build failed!'
        }
    }
}
