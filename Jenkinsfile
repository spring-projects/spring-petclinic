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
                git url: 'https://github.com/CChariot/spring-petclinic.git', branch: 'FinalProject_main', credentialsId: 'github-token'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    dockerImage = docker.build("spring-petclinic")
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    script {
                        dockerImage.inside("-u root") {
                            sh './mvnw sonar:sonar -Dsonar.projectKey=spring-petclinic'
                        }
                    }
                }
            }
        }

        stage('Build Application') {
            steps {
                script {
                    dockerImage.inside("-u root") {
                        sh './mvnw clean package -DskipTests'
                    }
                }
            }
        }

        stage('Run Application') {
            steps {
                script {
                    dockerImage.inside("-u root") {
                        sh 'java -jar target/*.jar'
                    }
                }
            }
        }

        stage('OWASP ZAP') {
            steps {
                sh '''
                docker run --rm -v $(pwd)/zap-report:/zap/wrk:rw \
                owasp/zap2docker-stable zap-baseline.py -t http://localhost:8080 \
                -g gen.conf -r zap-report.html
                '''
            }
        }

        stage('Publish ZAP Report') {
            steps {
                publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: false, reportDir: 'zap-report', reportFiles: 'zap-report.html', reportName: 'OWASP ZAP Report'])
            }
        }

        stage('Deploy to Production') {
            steps {
                sh 'ansible-playbook -i inventory/production deploy.yml'
            }
        }
    }

    post {
        always {
            script {
                try {
                    if (dockerImage != null) {
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