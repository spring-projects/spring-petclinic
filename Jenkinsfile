pipeline {
    agent any
    stages {
        stage('source code'){
            steps {
               git url: 'https://github.com/Moez786/spring-petclinic.git',
               branch: 'main'
            }
        }
        stage('build'){
            steps {
                sh 'mvn package'
            }
        }
        stage('Junit Results'){
            steps {
                junit '**/surefire-reports/*.xml'
                } 
            }   
    
        }

    }