pipeline {
    agent any
     tools {
        maven 'maven 3.6'
        jdk 'Java 1.'
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
