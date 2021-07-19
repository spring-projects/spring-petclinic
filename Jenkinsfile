pipeline {
    agent any
    tools {
        jdk 'JDK 1.8'
    }
    environment {
        SPRING_PROFILES_ACTIVE = "ci"
    }
    stages {
        stage('Initialize') {
            steps {
                sh "printenv"
            }
        }
        stage('Compile') {
            steps {
                withMaven(maven: 'M3', options: [artifactsPublisher(disabled: true), jacocoPublisher(disabled: true)]) {
                    sh "mvn clean compile"
                }
            }
        }
        stage('Package') {
            when {
                expression {
                    currentBuild.resultIsBetterOrEqualTo('SUCCESS')
                }
            }
            steps {
                withMaven(maven: 'M3', options: [jacocoPublisher(disabled: true)]) {
                    sh "mvn -DskipTests package"
                }
            }
        }
        stage('Deploy [Build & Push Docker container image]') {
            when {
                expression {
                    currentBuild.resultIsBetterOrEqualTo('SUCCESS')
                }
            }
            stage('Build image') {
                    app = docker.build("docker-registry:5000/petclinic")
            }
        }
    }
}