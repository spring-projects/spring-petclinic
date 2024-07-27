pipeline {
    agent any

    environment {
        SONARQUBE_URL = 'http://sonarqube:9000'
        SONARQUBE_CREDENTIALS_ID = 'admin' 
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh './mvnw clean install'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh './mvnw sonar:sonar -Dsonar.projectKey=spring-petclinic'
                }
            }
        }

        stage('OWASP ZAP') {
            steps {
                sh '''
                docker run --rm -v $(pwd)/zap-report:/zap/wrk:rw \
                -t owasp/zap2docker-stable zap-baseline.py -t http://petclinic:8080 \
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
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed.'
        }
    }
}
