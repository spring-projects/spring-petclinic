pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                echo 'Building..'
                sh 'docker login -u="natyramone" -p="polaco2312"'
                sh 'mvn -q package'
                sh 'docker build -t pet-clinic .'
                sh 'docker tag pet-clinic $DOCKER_USER/pet-clinic:latest'
                sh 'docker push $DOCKER_USER/pet-clinic:latest'
            }
        }
       
    }
}
