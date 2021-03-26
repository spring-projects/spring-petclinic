#!groovy

pipeline {
    environment {
        registry = "owlleg68/spring-petclinic"
        registryCredential = 'dockerhub_id'
        dockerImage = ''
    }
    
    agent {
        label 'builder'
    }
      
    tools {
        maven "maven" // You need to add a maven with name "3.6.3" in the Global Tools Configuration page
    }

    stages {
        stage('Checkout'){
            steps {
                checkout([$class: 'GitSCM', branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/owlleg6/spring-petclinic.git']]])
            }
        }


        stage("Build") {
            steps {
                sh 'pwd'
                sh 'mvn clean install'
                sh 'cp target/*.jar /home/owlleg6/builds/petclinic.jar'   
                
                //sh "mvn --version"
                //sh "sudo mvn clean install"
            }
        }

        stage('Build Docker image') {
            steps {
                script{
                    dockerImage = docker.build registry    
                }
                //sh 'mvn spring-boot:build-image'
                
            }
            
        }
            
        stage('Push Image') {
            steps {
                script {
                    docker.withRegistry( '', registryCredential ) {
                        dockerImage.push()
                    }
                }
            }
        }


        stage('Artifacts') {
            steps {
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true, followSymlinks: false
            }
        
        }
    }
    
    post {
        always {
            cleanWs()
        }
    }

}
