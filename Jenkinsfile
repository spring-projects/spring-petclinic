pipeline {
    agent any
     tools {
        maven 'maven 3.6.3'
        jdk 'jdk-1.8'
    }
    stages {
        stage('Build') {
            steps {
                sh 'mvn clean compile' 
            }
        }
        stage('Test') {
            steps {
                 sh 'mvn test'  
            }
        }
        stage('Package') {
            steps {
                 sh 'mvn package'  
            }
        }

        stage('Deploy') {
             /* only deploy when branch is master */
            when {
                branch 'master'
            }
            steps {
                sh 'mvn deploy' 
            }
        }

    }
}
