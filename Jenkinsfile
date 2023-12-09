pipeline {
    agent any
    stages {
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
