pipeline {
    // We use the Jenkins master agent, which has Docker socket access
    agent any
    
    // Use the Maven installation configured in Jenkins
    tools {
        maven 'M3' 
    }
    
    // Environment variable for the image tag
    environment {
        // Use the Jenkins build number as the image version tag
        IMAGE_NAME = "your-registry-username/sample-java-app"
        IMAGE_TAG = "${env.BUILD_NUMBER}"
    }

    stages {
        stage('Checkout Code') {
            steps {
                // SCM Checkout is automatic with 'Pipeline script from SCM'
                echo "Code checked out from: ${GIT_URL}"
            }
        }
        
        stage('Build & Test (Maven)') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }
        
        stage('Containerize App') {
            steps {
                // Build the Docker image using the Dockerfile in the workspace
                sh "docker build -t ${IMAGE_NAME}:${IMAGE_TAG} ."
            }
        }
        
        stage('Push Image to Registry') {
            steps {
                // Use the credentials configured in Step 9
                withCredentials([usernamePassword(credentialsId: 'docker-hub-creds', passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USER')]) {
                    sh "docker login -u ${DOCKER_USER} -p ${DOCKER_PASSWORD}"
                    sh "docker push ${IMAGE_NAME}:${IMAGE_TAG}"
                }
            }
        }
        
        stage('Provision/Apply Infrastructure (Terraform)') {
            steps {
                // IMPORTANT: Requires Terraform files in a 'terraform' directory in your repo
                dir('terraform') {
                    sh 'terraform init'
                    sh 'terraform apply -auto-approve'
                }
            }
        }
        
        stage('Deploy to Kubernetes (kubectl/Ansible)') {
            steps {
                // Assumes kubectl is available on the macOS host (via Docker Desktop)
                // and the Jenkins container can access it (often via the host's PATH or by running on the host itself)
                
                // 1. Apply the base deployment/service YAML (optional, can be done via Terraform)
                sh 'kubectl apply -f k8s-deployment.yaml' 
                
                // 2. Rollout the new image tag
                sh "kubectl set image deployment/java-app-deployment java-app-container=${IMAGE_NAME}:${IMAGE_TAG}"
                
                // OR (if using Ansible):
                // sh 'ansible-playbook -i localhost, deploy-k8s.yml'
            }
        }
    }
}
