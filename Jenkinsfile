pipeline {
    agent any
    stages {
        stage('pull from vcs') { 
        
           steps {
            git url: 'https://github.com/Qtalha/spring-petclinic.git',
            branch: 'rel-race' 
           }
        
        }
        stage("build") {
            steps {
                sh "export PATH=/usr/share/maven:$PATH"
                sh 'mvn pacakge'
            }
        }
    }
}
