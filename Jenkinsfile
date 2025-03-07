pipeline {
    agent any

    stages {
        stage('Checkout Code') {
            steps {
                echo 'Cloning repository from GitHub...'
                git branch: 'cicd-pipeline', url: 'https://github.com/Henry-0810/spring-petclinic_DevOps_Project.git'
            }
        }

        stage('Build & Test') {
            steps {
                echo 'Building and testing the Spring Boot application...'
                sh 'mvn clean package'
                sh 'mvn test'
            }
        }

//         stage('Code Quality Analysis - SonarCloud') {
//             steps {
//                 echo 'Running SonarCloud Analysis...'
//                 withCredentials([string(credentialsId: 'sonar-token', variable: 'SONAR_TOKEN')]) {
//                     sh '''
//                     mvn sonar:sonar \
//                         -Dsonar.projectKey=your-sonarcloud-project-key \
//                         -Dsonar.organization=your-sonarcloud-organization \
//                         -Dsonar.host.url=https://sonarcloud.io \
//                         -Dsonar.login=$SONAR_TOKEN
//                     '''
//                 }
//             }
//         }
//
//         stage('Security Scan - Trivy') {
//             steps {
//                 echo 'Running security scan on Docker image...'
//                 sh 'trivy image --exit-code 1 --severity HIGH,CRITICAL your-dockerhub-username/spring-petclinic || true'
//             }
//         }
//
//         stage('Build Docker Image') {
//             steps {
//                 echo 'Building Docker image...'
//                 sh 'docker build -t your-dockerhub-username/spring-petclinic .'
//             }
//         }
//
//         stage('Push to Docker Hub') {
//             steps {
//                 echo 'Pushing Docker image to registry...'
//                 withCredentials([usernamePassword(credentialsId: 'docker-hub-credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
//                     sh '''
//                     echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
//                     docker push your-dockerhub-username/spring-petclinic
//                     '''
//                 }
//             }
//         }
//
//         stage('Notify') {
//             steps {
//                 echo 'Sending Slack notification...'
//                 withCredentials([string(credentialsId: 'slack-webhook', variable: 'SLACK_WEBHOOK')]) {
//                     sh '''
//                     curl -X POST --data-urlencode "payload={\\"channel\\": \\"#devops\\", \\"username\\": \\"jenkins\\", \\"text\\": \\"Build successful!\\"}" $SLACK_WEBHOOK
//                     '''
//                 }
//             }
//         }
    }

//     post {
//         failure {
//             echo 'Pipeline failed! Sending failure notification...'
//             withCredentials([string(credentialsId: 'slack-webhook', variable: 'SLACK_WEBHOOK')]) {
//                 sh '''
//                 curl -X POST --data-urlencode "payload={\\"channel\\": \\"#devops\\", \\"username\\": \\"jenkins\\", \\"text\\": \\"Build failed! Check Jenkins logs.\\"}" $SLACK_WEBHOOK
//                 '''
//             }
//         }
//     }
}
