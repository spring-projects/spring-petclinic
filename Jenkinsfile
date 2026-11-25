pipeline {
    
    agent any
    
    environment {
        REGISTRY = "16.113.140.183:8082"
        IMAGE_NAME = "spring-petclinic"
        IMAGE_TAG = "${BUILD_NUMBER}"
    }
    
    stages {
        
        stage('Checkout') {
            steps {
                git credentialsId: 'github-creds',
                    branch: 'dev',
                    url: 'https://github.com/Siva-Kumar10/spring-petclinic.git'
            }
        }
        
        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }
        
        stage('SonarQube Scan') {
            steps {
                
                withCredentials([
                    string(
                        credentialsId: 'sonarqube-creds',
                        variable: 'SONAR_TOKEN'
                    )
                ])  {
                    
                    sh '''
                    mvn clean verify \
		    org.sonarsource.scanner.maven:sonar-maven-plugins:sonar \
                    -Dsonar.host.url=http:16.113.140.183:900 \
                    -Dsonar.token=\$SONAR_TOKEN
                    '''
                }
            }
        }
        
        stage('Docker Build') {
            steps {
                
                sh '''
                docker tag \
                ${IMAGE_NAME}:${BUILD_NUMBER} \
                ${REGISTRY}/${IMAGE_NAME}:${BUILD_NUMBER}
                '''
            }
        }
        
        stage('Docker Tag') {
            steps {
                
                sh '''
                docker tag \
                ${IMAGE_NAME}:${BUILD_NUMBER} \
                ${REGISTRY}/${IMAGE_NAME}:${BUILD_NUMBER}
                '''
            }
        }
        
        stage('Docker Push') {
            steps {
                
                withCredentials([
                    usernamePassword(
                        credentialsId: 'nexus-creds',
                        usernameVariable: 'NEXUS_USER',
                        passwordVariable: 'NEXUS_PASS'
                    )
                ])  {
                    
                    sh '''
                    echo \$NEXUS_PASS | docker login ${REGISTRY} \
                    -u \$NEXUS_USER \
                    --password-stdin
                    
                    docker push \
                    ${REGISTRY}/${IMAGE_NAME}:${BUILD_NUMBER}
                    '''
                }
            }
        }
        
        stage('Deploy To Kubernetes') {
            steps {
                
                sh '''
                kubectl set image deployment/petclinic \
       		 petclinic=16.113.140.183:8082/spring-petclinic:${BUILD_NUMBER} \
        	-n petclinic

       		 kubectl rollout status deployment/petclinic \
	        -n petclini
                '''
            }
        }
    }
    
    post {
        
        success {
            echo 'Pipeline Executed Successfully'
        }
        
        failure {
            echo 'Pipeline Failed'
        }
    }
}
 
