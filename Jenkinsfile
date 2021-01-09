pipeline {
    agent {
        label 'dockerbuild'
    }
    stages {
        stage('Building our image') {
            steps {
                script {
                    dockerImage = docker.build "mpatel011/spring-petclinic:$BUILD_NUMBER"
                }
            }
        }
        stage('Deploy our image') {
            steps {
                script {
                    docker.withRegistry('' , 'dockerhub') {
                        dockerImage.push()
                    }
                }
            }
        }
    }
}
