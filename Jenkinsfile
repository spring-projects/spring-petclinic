pipeline {
    agent {
        label 'mavenbuilder'
    }
    environment {
        TAG = sh(returnStdout: true, script: "git rev-parse --short HEAD").trim()
        DOCKER_REGISTRY = "https://hub.docker.com/repository/docker/rgeorgegrid/mr"
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
                    def composeBuildOutput = sh(script: 'docker-compose build --quiet', returnStdout: true).trim()
                    def imageIds = composeBuildOutput.tokenize('\n')

                    imageIds.each { imageId ->
                        def serviceName = sh(script: "docker inspect --format='{{index .RepoTags 0}}' $imageId | cut -d':' -f1", returnStdout: true).trim()
                        sh "docker tag $imageId $DOCKER_REGISTRY/$serviceName:$TAG"
                        withCredentials([usernamePassword(credentialsId: 'DOCKERHUB_CREDENTIALS_ID', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                            sh "docker login -u $DOCKER_USERNAME -p $DOCKER_PASSWORD $DOCKER_REGISTRY"
                            sh "docker push $DOCKER_REGISTRY/$serviceName:$TAG"
                            echo 'PUSHED TO $DOCKER_REGISTRY/$serviceName:$TAG'
                            echo $DOCKER_REGISTRY $DOCKER_PASSWORD $DOCKER_USERNAME
                        }
                    }
                }
            }
        }
    }
}
