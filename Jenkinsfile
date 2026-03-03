pipeline {
    agent any

    environment {
        // Ensure Jenkins can find docker and docker-compose
        PATH = "/usr/bin:/usr/local/bin:/usr/local/sbin:/usr/sbin:${env.PATH}"
        DOCKERHUB_CREDENTIALS = credentials('dockerhub-creds') // Add your DockerHub credentials in Jenkins
    }

    stages {
        stage('Checkout Code') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/PipelineNinja/spring-petclinic.git'
            }
        }

        stage('Build Application') {
            steps {
                sh 'mvn clean install -DskipTests'
            }
        }

        stage('Run Tests') {
            steps {
                sh 'mvn test'  // Testcontainers will start Postgres automatically
            }
        }

        stage('Archive Artifacts') {
            steps {
                archiveArtifacts artifacts: '**/target/*.jar', allowEmptyArchive: true
            }
        }

        stage('Build Docker Image') {
            steps {
                sh '''
                    docker build -t kishormore123/spring-petclinic:latest .
                '''
            }
        }

        stage('Login to DockerHub') {
            steps {
                sh "echo ${DOCKERHUB_CREDENTIALS_PSW} | docker login -u ${DOCKERHUB_CREDENTIALS_USR} --password-stdin"
            }
        }

        stage('Push Docker Image') {
            steps {
                sh 'docker push kishormore123/spring-petclinic:latest'
            }
        }
    }

    post {
        always {
            cleanWs()
        }
        success {
            echo "✅ Pipeline completed successfully!"
        }
        failure {
            echo "❌ Pipeline failed!"
        }
    }
}
