pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                echo 'Building..'
                docker login -u="natyramone" -p="polaco2312"

                mvn -q package

                docker build -t pet-clinic .
                docker tag pet-clinic $DOCKER_USER/pet-clinic:latest
                docker push $DOCKER_USER/pet-clinic:latest
            }
        }
       
    }
}
