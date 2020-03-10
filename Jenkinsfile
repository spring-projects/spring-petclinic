pipeline {
    agent any
	tools {
        maven 'maven 3.6.3'
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
		stage('Deploy') {
            when {
                branch 'master'
            }
            steps {
                sh 'mvn deploy' 
            }
        }
    }
}
