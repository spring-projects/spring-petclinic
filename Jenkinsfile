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
                    sh 'docker build -t spring-perclinic:1.0 .'
                }
            }
        }       

        stage('Push Docker Image') {
            steps {
                echo "Push Docker Image to ECR"
            }
        }
        stage('Upload to S3') {
            steps {
                echo "Upload to S3"                
                }
            }
        }
        
        stage('Codedeploy Workload') {
            steps {
                echo "create application"                
            }
        }        
    }
}
