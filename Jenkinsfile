pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                echo 'Building..'
                sh("ls -la")
            }
        }
        stage('Test') {
            steps {
                echo 'Testing..'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying....'
            }
        }
    }
}
