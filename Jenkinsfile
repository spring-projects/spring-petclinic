pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                echo 'Running build automation'
                sh './mvnw package'
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
     stage('Push Docker Image') {
        when {
            branch 'main'
        }
        steps {
            script {
                docker.withRegistry('https://registry.hub.docker.com', 'docker_hub_login') {
                    app.push("${env.BUILD_NUMBER}")
                    app.push("latest")
                }
            }
        }
    }
}
}
