pipeline {
    agent any
    stages {
        stage('Checkstyle') {
            steps {
                sh 'mvn checkstyle:checkstyle'
            }
            post {
                always {
                    archiveArtifacts artifacts: '**/target/checkstyle-result.xml', allowEmptyArchive: true
                }
            }
        }
        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }
        stage('Build Without Tests') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }
        stage('Create Docker Image and Push') {
            steps {
                script {
                    def commitId = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
                    sh "docker build -t your-docker-hub-username/spring-petclinic:${commitId} ."
                    sh "docker push your-docker-hub-username/spring-petclinic:${commitId}"
                }
            }
        }
    }
}
