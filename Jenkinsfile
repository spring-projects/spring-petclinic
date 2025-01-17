pipeline {
    agent {
        docker {
            image 'docker:latest'
            args '--privileged -v /var/lib/docker'
        }
    }
    environment {
        DOCKER_CREDENTIALS_ID = 'dockerhub-credentials'
    }
    stages {
        stage('Checkstyle') {
            steps {
                sh './mvnw checkstyle:checkstyle'
                archiveArtifacts artifacts: 'target/checkstyle-result.xml', fingerprint: true
            }
        }
        stage('Test') {
            steps {
                sh './mvnw test'
            }
        }
        stage('Build') {
            steps {
                sh './mvnw clean package -DskipTests'
            }
        }
        stage('Create Docker Image') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: "${DOCKER_CREDENTIALS_ID}", usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                        sh """
                            echo $DOCKER_PASSWORD | docker login -u $DOCKER_USERNAME --password-stdin
                            docker build -t marijastopa/mr-jenkins:\${GIT_COMMIT:0:7} .
                            docker push marijastopa/mr-jenkins:\${GIT_COMMIT:0:7}
                        """
                    }
                }
            }
        }
    }
}
