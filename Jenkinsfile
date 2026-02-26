pipeline {
    agent any

    triggers {
        // Trigger every 5 minutes, only on Thursdays (4 = Thursday in cron)
        cron('H/5 * * * 4')
    }

    tools {
        // Ensure Maven and JDK are configured in Jenkins Global Tool Configuration
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
                sh 'mvn clean compile -DskipTests'
            }
        }

        stage('Test & Code Coverage (JaCoCo)') {
            steps {
                echo 'Running tests and generating JaCoCo coverage report...'
                sh 'mvn test jacoco:report'
            }
            post {
                always {
                    // Publish JaCoCo coverage report in Jenkins UI
                    jacoco(
                        execPattern: '**/target/jacoco.exec',
                        classPattern: '**/target/classes',
                        sourcePattern: '**/src/main/java',
                        exclusionPattern: '**/src/test*',
                        changeBuildStatus: false,
                        minimumInstructionCoverage: '0',
                        minimumBranchCoverage: '0',
                        minimumComplexityCoverage: '0',
                        minimumLineCoverage: '0',
                        minimumMethodCoverage: '0',
                        minimumClassCoverage: '0'
                    )
                }
            }
        }

        stage('Package') {
            steps {
                echo 'Packaging the application artifact...'
                sh 'mvn package -DskipTests'
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
            // Publish JUnit test results
            junit '**/target/surefire-reports/*.xml'
        }
        success {
            echo 'Build succeeded!'
        }
        failure {
            echo 'Build failed!'
        }
    }
}
