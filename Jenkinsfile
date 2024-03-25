pipeline {
    agent {
        label 'mavenbuilder'
    }
    stages {
        stage ('Checkstyle') {
            steps {
                script {
                    echo 'RUNNING CHECKSTYLES...'
                    sh 'mvn checkstyle:checkstyle'
                }
            }
        }
        stage ('Test') {
            steps {
                script {
                    echo 'RUNNING TESTS...'
                    sh 'mvn test'
                }
            }
        }
        stage ('Build') {
            steps {
                script {
                    echo 'BUILDING ARTIFACTS...'
                    sh 'mvn clean package'
                }
            }
        }
        stage ('Containerisation') {
            steps {
                script {
                    def dockerImage = docker.build("test-image1:${GIT_COMMIT[0..7]}", '.')
                    docker.withRegistry('https://hub.docker.com/repository/docker/rgeorgegrid/mr', 'docker_hub_login') {
                        dockerImage.push()
                    }
                    echo 'IMAGES BUILT AND PUSHED TO MR REGISTRY'
                }
            }
        }
    }
}
