pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                echo 'Running build automation'
                sh './mvnw package
            }
        }           
    stage('Build Docker Image') {
         when {
             branch 'main'
         }
         steps {
             script {
                 app = docker.build("sprientera/pets")
                 app.inside {
                   sh 'echo $(curl localhost:80)'
                 }
             }
         }     
     }
 }
