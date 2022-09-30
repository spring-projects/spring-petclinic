pipeline {
    agent any
    stages {
        stage('pull from vcs') { 

            tools {
              maven 'mvn_3.6.0' 
    }
        
           steps {
            git url: 'https://github.com/Qtalha/spring-petclinic.git',
            branch: 'rel-race' 
           }
        
        }
        stage("build") {
            steps {
                maven 'apache-maven-3.6.0'
                sh 'mvn_3.6'
            }
        }
    }
}
