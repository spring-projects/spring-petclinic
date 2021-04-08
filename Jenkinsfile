pipeline {
    agent any

    stages {
        stage('Checkout SCM') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                echo 'Running build automation'
                sh '''
                    ./mvnw package
                   '''
           }
        }
        stage('CREATE ARTIFACT') {
            steps {
                echo 'Creating Docker Image...'
                sh '''
                     docker build . -t rodley/pet-clinic:${BUILD_NUMBER} -f Dockerfile
                   '''
            }
        }
        stage('Push artifact to docker registry') {
            steps {
               script {
                    def dockerImage = docker.build("${env.IMAGE_NAME}", "-f ${env.DOCKERFILE_NAME} .")
                    docker.withRegistry('', 'dockerhub-creds') {
                    dockerImage.push()
                    dockerImage.push("latest")
                       } 
                    echo "Pushed Docker Image: ${env.IMAGE_NAME}"
                      }
                    sh "docker rmi ${env.IMAGE_NAME} ${env.IMAGE_NAME_LATEST}"
            }
        }
        
        stage('Deploy') {
            steps {
                echo 'Deploying....'
            }
        }
    }
}
