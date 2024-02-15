pipeline {
    agent any
    
    stages {
        stage('Checkstyle') {
            when {
                expression { env.CHANGE_TARGET ==~ /^merge$/ }
            }
            steps {
                // Use Maven checkstyle plugin to generate a code style report
                sh 'mvn checkstyle:checkstyle'
                archiveArtifacts artifacts: '**/target/checkstyle-result.xml', fingerprint: true
            }
        }
        stage('Test') {
            when {
                expression { env.CHANGE_TARGET ==~ /^merge$/ }
            }
            steps {
                // Run tests with Maven
                sh 'mvn test'
            }
        }
        stage('Build') {
            when {
                expression { env.CHANGE_TARGET ==~ /^merge$/ }
            }
            steps {
                // Build without tests with Maven
                sh 'mvn clean package -DskipTests'
            }
        }
        stage('Create Docker Image MR') {
            when {
                expression { env.CHANGE_TARGET ==~ /^merge$/ }
            }
            steps {
                // Build Docker image from Dockerfile
                script {
                    def gitCommitShort = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
                    def dockerImage = "mihailinternul/mr:${gitCommitShort}"
                    sh "docker build -t ${dockerImage} ."
                    sh "docker push ${dockerImage}"
                }
            }
        }
        stage('Create Docker Image Main') {
            when {
                expression { env.CHANGE_TARGET ==~ /^main$/ }
            }
            steps {
                // Build Docker image from Dockerfile
                script {
                    def gitCommitShort = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
                    def dockerImage = "mihailinternul/main:${gitCommitShort}"
                    sh "docker build -t ${dockerImage} ."
                    sh "docker push ${dockerImage}"
                }
            }
        }
    }
}
