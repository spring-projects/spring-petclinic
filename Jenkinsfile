pipeline {
    agent any
    
    stages {
        // Stage 1: Compile
        stage('Compile') {
            steps {
                script {
                    sh 'mvn compile'
                }
            }
        }

        // Stage 2: Run Tests
        stage('Run Tests') {
            steps {
                script {
                    sh 'mvn test'
                }
            }
        }

        // Stage 3: Package as Docker Image
        stage('Package as Docker Image') {
            steps {
                script {
                    sh 'mvn package'
                    sh 'docker build -t spring-petclinic .'
                }
            }
        }
    }
}