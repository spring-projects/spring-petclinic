pipeline {
    agent any

    tools {
        jdk 'jdk17'
        maven 'maven'
    }

    environment {
        SONAR_TOKEN = credentials('edf40d2f41d32dc9a7e0a42cb45aa683e645760c')
        SONAR_HOST  = 'http://localhost:9000'  // SonarQube local
        IMAGE       = 'spring-petclinic'        // Nom local de l'image Docker
        TAG         = 'latest'
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/zinebmouman/resevation_devices.git'
            }
        }

        stage('Build & Unit Tests') {
            steps {
                dir('backend') {
                    bat 'mvn -B -U clean verify'
                }
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: 'backend/target/surefire-reports/*.xml'
                    archiveArtifacts artifacts: 'backend/target/*.jar', fingerprint: true
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                dir('backend') {
                    bat """
                        mvn sonar:sonar ^
                          -Dsonar.projectKey=resevation_devices ^
                          -Dsonar.host.url=%SONAR_HOST% ^
                          -Dsonar.login=%SONAR_TOKEN%
                    """
                }
            }
        }

        stage('Build Docker locally') {
            steps {
                dir('backend') {
                    bat """
                        docker build -t %IMAGE%:%TAG% .
                    """
                }
            }
        }

        stage('Run Docker locally') {
            steps {
                bat """
                    docker stop %IMAGE% || exit 0
                    docker rm %IMAGE% || exit 0
                    docker run -d -p 8080:8080 --name %IMAGE% %IMAGE%:%TAG%
                """
            }
        }
    }

    post {
        success { echo "Pipeline OK → Docker local lancé : %IMAGE%:%TAG%" }
        failure { echo "Pipeline KO" }
    }
}
