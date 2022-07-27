pipeline {
    agent { label 'docker-builder'}
    environment {
        DOCKERHUB_CREDENTIALS = credentials('jenkins_docker_hub_login')
        }
    stages {
        stage('SCM') {
            steps {
                git branch: 'main', url: 'https://github.com/Shri-1991/spring-petclinic.git'
            }
        }
        stage('build') {
            steps {
                sh 'docker build -t spc:1.0 .'
            }
        }
        stage('dockerhublogin') {
            steps {
                sh 'echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin'
                sh 'docker tag spc:1.0 shri1991/spring'
                sh 'docker push shri1991/spring'
                sh 'docker image rm shri1991/spring'
            }
        }
        stage('k8s_deploy') {
            steps {
                sh 'kubectl apply -f spring.yml'
            }
        }
    }
}
