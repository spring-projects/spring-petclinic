pipeline {
    agent any

    stages {

        stage('Clone Code') {
            steps {
                git branch: 'main', url: 'https://github.com/ashishj7744/spring-petclinic.git'
            }
        }

        stage('Build') {
              steps {
                sh 'mvn clean package -DskipTests'
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
