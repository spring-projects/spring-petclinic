pipeline {
    agent any

    stages {

        stage('Clone Code') {
            steps {
                git 'https://github.com/your-repo/java-app.git'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t java-app .'
            }
        }

        stage('Run Container') {
            steps {
                sh '''
                docker stop java-container || true
                docker rm java-container || true
                docker run -d -p 9090:9090 --name java-container java-app
                '''
            }
        }

    }
}
