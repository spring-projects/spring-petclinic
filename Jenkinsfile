pipeline {
    agent { label 'worker1' }
    stages {
        stage('Current dir') {
            steps {
                sh 'pwd'
            }
        }
        stage('Build') {
            steps {
                sh './mvnw clean install'
            }        
        }
 
        stage('Docker Build') {
            steps {
      	        sh 'docker build -t spring-petclinic:latest .'
            }
        }
    }
    

}
