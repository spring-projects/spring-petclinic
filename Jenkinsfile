pipeline {
    agent { label 'worker1' }
    stages {
        stage('Maven install') {
            agent {
                docker {
                    image 'maven:3.5.0'
                }
            }
            steps {
                sh 'mvn clean install'
            }        
        }
        stage('Docker Build') {
    	    agent any
            steps {
      	        sh 'docker build -t spring-petclinic:latest .'
            }
        }
    }
    

}
