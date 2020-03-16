pipeline {
    agent any
    tools { maven 'Maven 3.6.3' jdk 'Java 9.0.4' }
    stages {
        stage('Build') {
            steps {
                sh 'mvn clean' 
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
             when {
                 branch 'master'
             }
        }
    }
}
