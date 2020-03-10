pipeline {
    agent any
	tools {
        maven 'Maven 3.6.3'
        jdk 'jdk-13'
    }
    stages {
        stage('Build') {
            steps {
                sh 'mvn clean compile' 
            }
        }
		stage('Test') {
            steps {
                sh 'mvn test' 
            }
        }
		stage('Package') {
            steps {
                sh 'mvn package' 
            }
        }
    }
}
