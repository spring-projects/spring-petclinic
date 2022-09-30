pipeline {
    agent any
    stages {
        stage('pull from vcs') { 

            tools {
        maven 'apache-maven-3.0.1' 
    }
        
           steps {
            git url: 'https://github.com/Qtalha/spring-petclinic.git',
            branch: 'rel-race' 
           }
        
        }
        stage("build") {
            steps {
                maven 'apache-maven-3.0.1'
                sh 'mvn_3.6'
            }
        }
    }
}
