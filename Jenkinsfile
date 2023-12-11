pipeline {

    agent any

    environment {
        // Define environment variable using credentials
        DOCKER_CREDENTIALS = credentials('dockercreds')
    }

    stages {
        stage('Docker Login') {
            steps {
                script {
                // Perform Docker login using credentials
                    withEnv(["DOCKER_USERNAME=${DOCKER_CREDENTIALS_USR}", "DOCKER_PASSWORD=${DOCKER_CREDENTIALS_PSW}"]) {
                    sh "docker login -u $DOCKER_USERNAME -p $DOCKER_PASSWORD"
                        }
                    }
                }
        }
        stage('Checkstyle') {
            steps {
                sh './mvnw checkstyle:checkstyle'
            }
            post {
                always {
                    archiveArtifacts artifacts: '**/target/checkstyle-result.xml', allowEmptyArchive: true
                }
            }
        }
        stage('Test') {
            steps {
                sh './mvnw test'
            }
        }
        stage('Build Without Tests') {
            steps {
                sh './mvnw clean package -DskipTests'
            }
        }
        stage('Create Docker Image and Push') {
            steps {
                script {
                    def commitId = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
                    sh "docker build -t mivancevic/spring-petclinic:${commitId} ."
                    sh "docker push mivancevic/spring-petclinic:${commitId}"
                }
            }
        }

    }
}
