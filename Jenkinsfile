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
                sh 'terraform validate'
                sh 'terraform apply -auto-approve'
            }
        }
    }
}
