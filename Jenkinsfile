pipeline {
    agent any
     tools {
        maven 'maven 3.6.3'
        jdk 'jdk-1.8'
    }
    stages {
        stage('Build') {
            steps {
                sh './mvnw clean compile' 
            }
        }
        stage('Test') {
            steps {
                 sh './mvnw test'  
            }
        }
        stage('Package') {
            steps {
                 sh './mvnw package'  
            }
        }

        stage('Deploy') {
             /* only deploy when branch is master */
            when {
                branch 'master'
            }
            steps {
                sh './mvnw deploy' 
            }
        }

    }

