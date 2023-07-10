pipeline {
    agent any
    tools {
        maven 'M3'
        jdk 'JDK11'
    }   
    environment {
        AWS_CREDENTIAL_NAME = "AWSCredentials"
        REGION = "ap-northeast-2"
        DOCKER_IMAGE_NAME="spring-petclinic"
        DOCKER_TAG="1.0"
        ECR_REPOSITORY = "25730763417.dkr.ecr.ap-northeast-2.amazonaws.com"
        ECR_DOCKER_IMAGE = "${ECR_REPOSITORY}/${DOCKER_IMAGE_NAME}"
        ECR_DOCKER_TAG = "${DOCKER_TAG}"
    }
    
    stages {
        stage('Git clone') {
            steps {
                git url: 'https://github.com/s4616/spring-petclinic.git', branch: 'efficient-webjars', credentialsId: 'admin'
            }
            post {
                success {
                    echo 'success clone project'
                }
                failure {
                    error 'fail clone project' // exit pipeline
                }
            }
        }        
        stage ('mvn Build') {
            steps {
                sh 'mvn -Dmaven.test.failure.ignore=true install' 
            }
            post {
                success {
                    junit '**/target/surefire-reports/TEST-*.xml' 
                }
            }
        }        
        stage ('Docker Build') {
            steps {
                dir("${env.WORKSPACE}") {
                    sh 'docker build -t aws00-spring-petclinic:1.0 .'
                }
            }
        }
        stage('Push Docker Image') {
            steps {
                echo "Push Docker Image to ECR"  
                script{
                    // cleanup current user docker credentials
                    sh 'rm -f ~/.dockercfg ~/.docker/config.json || true'                    
                   
                    docker.withRegistry("https://${ECR_REPOSITORY}", "ecr:${REGION}:${AWS_CREDENTIAL_NAME}") {
                      docker.image("${ECR_DOCKER_IMAGE}:${ECR_DOCKER_TAG}").push()
                    }
                }
            }
        }
        stage('Upload to S3') {
            steps {
                echo "Upload to S3"                
            }
        }
        
        stage('Codedeploy Workload') {
            steps {
                echo "create application"                
            }
        } 
    }
}
