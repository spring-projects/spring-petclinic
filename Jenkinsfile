pipeline {
    agent { label 'worker1' }
    stages {
        stage('Build') {
            steps {
                sh 'mvn clean install'
            }        
        }
        stage('Docker Build') {
            steps {
      	        sh 'docker build -t spring-petclinic:latest .'
            }
        }
    }
    

}
