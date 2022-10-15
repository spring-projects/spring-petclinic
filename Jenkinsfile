
pipeline {
    agent {label 'terraform'}
    stages {
        stage('pull from vcs') {
        steps {
            git url: 'https://github.com/Qtalha/spring-petclinic.git',
            branch: 'rel-race' 
           }
        
        }
        stage("build") {
            steps {
                sh 'terraform init'
            }
        }
        stage("apply") {
            steps {
                sh "terraform validate"
                sh "terraform apply -auto-approve"
            }
        }
    }
}
