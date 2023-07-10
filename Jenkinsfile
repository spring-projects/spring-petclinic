pipeline {
    agent any
    tools {
        maven 'M3'
        jdk 'JDK11'
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
                    sh 'docker build -t spring-petclinic:1.0 .'
                }
            }
        }
        stage('Push Docker Image') {
            steps {
                echo "Push Docker Image to ECR"  
                script {
                    // cleanup current user docker credentials
                    sh 'rm -f ~/.dockercfg || true'
                    sh 'rm -f ~/.docker/config.json || true' 
                    docker.withRegistry("https://257307634175.dkr.ecr.ap-northeast-2.amazonaws.com/aws00-spring-petclinic", "ecr:ap-northeast-2:AWSCredentials") {
                        docker.image("spring-petclinic:1.0").push()
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
