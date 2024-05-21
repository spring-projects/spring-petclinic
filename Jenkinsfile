pipeline {
    agent any
    
    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/spring-projects/spring-petclinic.git'
            }
        }
        
        stage('Build') {
            steps {
                // Using Maven to build the project
                sh './mvnw clean install'
            }
        }
        
        stage('Test') {
            steps {
                // Run tests
                sh './mvnw test'
            }
        }
        
        stage('Package') {
            steps {
                // Package the application
                sh './mvnw package'
            }
        }
    }
    
    post {
        always {
            archiveArtifacts artifacts: 'target/*.jar', allowEmptyArchive: true
            junit 'target/surefire-reports/*.xml'
        }
    }
}
