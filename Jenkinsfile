pipeline {
    environment {
    }
	
	tools {
	    maven 'Maven 3.5.0'
	}

    agent none

    stages {
        stage('Build, Test, and Package') {
            agent any
            steps {
                sh "mvn clean package"
            }
        }
    }
}