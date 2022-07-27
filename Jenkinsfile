pipeline {
    agent { label 'docker-builder'}
    stages {
        stage('SCM') {
            steps {
                git branch: 'qa', url: 'https://github.com/Shri-1991/spring-petclinic.git'
            }
        }
        stage('k8s_deploy') {
            steps {
                sh 'kubectl apply -f spring.yml'
            }
        }
    }
}
