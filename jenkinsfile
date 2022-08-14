pipeline {
    agent none
    stages {
        stage('Source Code') {
            agent { label 'docker' && 'kubeclust' }
            steps {
                git url: 'https://github.com/nikhatsultana639/spring-petclinic.git', 
                branch: 'main'
            }
        }
        stage('build image using docker file') {
            agent { label 'docker' }
            steps {
                sh 'sudo docker image build -t spc:1.0 .'
            }
        }
        stage('push image to docker hub') {
            agent { label 'docker' }
            steps {
                withCredentials([string(credentialsId: 'dockerhubsec', variable: 'dockerhubsec')]) {
                sh 'sudo docker login -u nikhatsultana -p ${dockerhubsec}'
                }
                sh 'sudo docker tag spc:1.0 nikhatsultana/spc:1.0'
                sh 'sudo docker push nikhatsultana/spc:1.0'
            }
        } 
        stage('deploy to kubeclust') {
            agent { label 'kubeclust' }
            steps {
                sh 'kubectl apply -f spcdeployservice.yaml' 
            }
        }
        stage('deploy to production env') {
            agent { label 'kubeclust' }
            steps {
                sh 'kubectl create deployment prod-pod --image=nikhatsultana/spc:1.0  -n=production --replicas=2'
            }
        }
        stage('deploy to developer env') { 
            agent { label 'kubeclust' }
            steps {
                sh 'kubectl create deployment dev-pod --image=nikhatsultana/spc:1.0  -n=developer --replicas=2'
           }
        }
        stage('deploy to qa env') {
            agent { label 'kubeclust' }
            steps {
                sh 'kubectl create deployment test-pod --image=nikhatsultana/spc:1.0  -n=qa --replicas=2'
            }
        }
    }
  }
