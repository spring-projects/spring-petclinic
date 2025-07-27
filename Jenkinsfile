pipeline {
    agent none

    tools {
        maven 'Maven 3.9.11'
    }

    environment {
        SONAR_TOKEN = credentials('ubuntu')
    }

    stages {
        stage('Clone GitHub Repo') {
            agent { label 'Agent2' }
            steps {
                git branch: 'main', url: 'https://github.com/AnkitaJaiswal-git/spring-petclinic.git'
            }
        }

        stage('SonarQube Analysis') {
             agent { label 'MandS' }
             steps {
                withSonarQubeEnv('SonarQube') {
                    sh """
                        mvn clean verify sonar:sonar \
                        -Dsonar.projectKey=spring-petclinic \
                        -Dsonar.login=$SONAR_TOKEN
                    """
                }
            }
        }

        stage('Build with Maven') {
            agent { label 'MandS' }
            steps {
                sh 'mvn package -DskipTests'
            }
        }

        stage('Transfer Artifact to AWS VM') {
            agent { label 'MandS' }
            steps {
                sshagent(credentials: ['aws-ssh-key']) {
                    sh '''
                        scp -o StrictHostKeyChecking=no target/*.jar ubuntu@34.229.183.22:/home/ubuntu/deployments/
                        echo "Running ls -ltr on remote server..."
                        ssh -o StrictHostKeyChecking=no ubuntu@34.229.183.22 \
                        "cd /home/ubuntu/deployments/ && ls -ltr"
                    '''
                }
            }
        }
    }

    post {
        success {
            echo "🎉 Deployment pipeline completed successfully."
        }
        failure {
            echo "❌ Pipeline failed. Check logs."
        }
    }
}
