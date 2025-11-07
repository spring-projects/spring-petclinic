pipeline {
    agent any
    tools {
        jdk 'Java 21'
        maven 'Maven 3.9.9'
    }
    stages {
        stage('Checkout') {
            steps {
                git branch: 'java21-petclinic', url: 'https://github.com/spring-projects/spring-petclinic.git'
            }
        }
        stage('Build') {
            steps {
                sh 'mvn clean package -B -DskipTests'
            }
        }
        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploy stage can be added here'
            }
        }
    }
}
