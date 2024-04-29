pipeline {
    agent any
    environment {
        NEXUS_CREDS = credentials('nexus-cred')
        NEXUS_DOCKER_REPO = '54.170.162.132:8085'
    }

    stages {
       
       stage('Docker Build') {
        
            steps { 
                    echo 'Building docker Image'
                    sh 'docker build -t $NEXUS_DOCKER_REPO/spring-petclinic:${GIT_COMMIT} .'
                }
        }

       stage('Docker Login') {
            steps {
                echo 'Nexus Docker Repository Login'
                script{
                    withCredentials([usernamePassword(credentialsId: 'nexus-cred', usernameVariable: 'USER', passwordVariable: 'PASS' )]){
                       sh ' echo $PASS | docker login -u $USER --password-stdin $NEXUS_DOCKER_REPO'
                    }
                   
                }
            }
        }

        stage('Docker Push') {
            steps {
                echo 'Pushing Imgaet to docker hub'
                sh 'docker push $NEXUS_DOCKER_REPO/spring-petclinic:${GIT_COMMIT}'
            }
        }
    }
}