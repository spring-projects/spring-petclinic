pipeline {
    agent any

    environment {
        SONARQUBE_URL = 'http://sonarqube:9000'
        SONARQUBE_CREDENTIALS_ID = 'admin'
        GITHUB_TOKEN = credentials('github-token')
    }

    stages {
        stage('Checkout') {
            steps {
                script {
                    echo "Checking out code..."
                    git url: 'https://github.com/CChariot/spring-petclinic.git', branch: 'FinalProject_main', credentialsId: 'github-token'
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    echo "Building Docker Image..."
                    dockerImage = docker.build("spring-petclinic")
                    echo "Docker Image built: ${dockerImage.id}"
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                script {
                    echo "Starting SonarQube analysis..."
                    withSonarQubeEnv('SonarQube') {
                        dockerImage.inside("-u root") {
                            sh './mvnw sonar:sonar -Dsonar.projectKey=spring-petclinic'
                        }
                    }
                    echo "SonarQube analysis completed."
                }
            }
        }

        stage('Build Application') {
            steps {
                script {
                    echo "Building application..."
                    dockerImage.inside("-u root") {
                        sh './mvnw clean package -DskipTests'
                    }
                    echo "Application build completed."
                }
            }
        }

        stage('Run Application') {
            steps {
                script {
                    echo "Running application..."
                    dockerImage.inside("-u root") {
                        sh 'java -jar target/*.jar'
                    }
                    echo "Application is running."
                }
            }
        }

        stage('OWASP ZAP') {
            steps {
                script {
                    echo "Running OWASP ZAP..."
                    sh '''
                    docker run --rm -v $(pwd)/zap-report:/zap/wrk:rw \
                    owasp/zap2docker-stable zap-baseline.py -t http://localhost:8080 \
                    -g gen.conf -r zap-report.html
                    '''
                    echo "OWASP ZAP analysis completed."
                }
            }
        }

        stage('Publish ZAP Report') {
            steps {
                script {
                    echo "Publishing OWASP ZAP report..."
                    publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: false, reportDir: 'zap-report', reportFiles: 'zap-report.html', reportName: 'OWASP ZAP Report'])
                    echo "OWASP ZAP report published."
                }
            }
        }

        stage('Deploy to Production') {
            steps {
                script {
                    echo "Deploying to production..."
                    sh 'ansible-playbook -i inventory/production deploy.yml'
                    echo "Deployment to production completed."
                }
            }
        }
    }

    post {
        always {
            script {
                try {
                    if (dockerImage != null) {
                        echo "Stopping and removing Docker Image..."
                        dockerImage.stop()
                        dockerImage.remove()
                    }
                } catch (Exception e) {
                    echo "Error during cleanup: ${e.message}"
                }
            }
        }
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed.'
        }
    }
}
