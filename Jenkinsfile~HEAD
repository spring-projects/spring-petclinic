pipeline {
    agent any  // This runs the pipeline on any available agent

    environment {
        // Set environment variables if needed
        APP_DIR = "/home/vagrant/petclinic"
        REPO_URL = "https://github.com/yiting68/spring-petclinic.git"
    }

    stages {
        stage('Checkout') {
            steps {
                // Checkout the latest code from the Git repository
                git branch: 'main', url: 'https://github.com/yiting68/spring-petclinic.git'
            }
        }

        stage('Build') {
            steps {
                // Install dependencies and build the project
                sh './gradlew clean build'  // assuming you are using Gradle
            }
        }

        stage('Test') {
            steps {
                // Run tests (JUnit tests in this case)
                sh './gradlew test'
            }
        }

        stage('Deploy') {
            steps {
                // Deploy the app, e.g., run the JAR file or deploy to your server
                sh 'vagrant up --provision'
            }
        }
    }

    post {
        always {
            // Actions to perform after the pipeline finishes
            echo 'Cleaning up'
            deleteDir() // Clean up the workspace after the build 
        }

        success {
            // Actions if the pipeline is successful
            echo 'Build and deploy succeeded!'
        }

        failure {
            // Actions if the pipeline fails
            echo 'Build failed!'
        }
    }
}