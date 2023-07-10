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
        ECR_REPOSITORY = "257307634175.dkr.ecr.ap-northeast-2.amazonaws.com"
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
                    sh 'docker build -t ${ECR_DOCKER_IMAGE}:${ECR_DOCKER_TAG} .'
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
                dir("${env.WORKSPACE}") {
                    sh 'zip -r deploy-1.0.zip ./scripts appspec.yml'
                    sh 'aws s3 cp --region ap-northeast-2 --acl private ./deploy-1.0.zip s3://aws00-codedeploy'
                    sh 'rm -rf ./deploy-1.0.zip'
                }
            }
        }
        
        stage('Codedeploy Workload') {
            steps {
                echo "create application"
                sh 'aws deploy create-application --application-name aws00-spring-petclinic'
               
                echo "create Codedeploy group"   
                sh '''
                    aws deploy create-deployment-group \
                    --application-name aws00-spring-petclinic \
                    --auto-scaling-groups aws00-spring-petclinic \
                    --deployment-group-name aws00-spring-petclinic \
                    --deployment-config-name CodeDeployDefault.OneAtATime \
                    --service-role-arn arn:aws:iam::257307634175:role/aws00-code-deploy-service-role
                    '''
                echo "Codedeploy Workload"   
                sh '''
                    aws deploy create-deployment --application-name project00 \
                    --deployment-config-name CodeDeployDefault.OneAtATime \
                    --deployment-group-name aws00-spring-petclinic \
                    --s3-location bucket=aws00-codedeploy,bundleType=zip,key=deploy-1.0.zip
                    '''
                    sleep(10) // sleep 10s
            }
            }
        } 
    }
}
