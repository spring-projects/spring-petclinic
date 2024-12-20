pipeline {
    agent any

    environment {
        GIT_COMMIT = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
        DOCKER_CREDS = credentials('docker_key')
        DOCKER_HOST: "tcp://docker:2375"
        DOCKER_TLS_CERTDIR: ""
    }

    stages {
        stage ('Checkstyle') {
            steps {
                sh 'mvn checkstyle:checkstyle'
                archiveArtifacts artifacts: 'target/checkstyle-report.xml', allowEmptyArchive: true
            }
        }
        stage ('Test') {
            steps {
                sh 'mvn test'
            }
        }
        stage ('Build') {
            steps {
                sh 'mvn clean package -DskipTests'
                archiveArtifacts artifacts: 'target/*.jar', allowEmptyArchive: false
            }
        }
        stage ('Creating Docker image') {
            steps {
                sh 'docker build -t vkarpenko02/mr:${GIT_COMMIT} .'
                withCredentials([usernamePassword(credentialsId: 'docker_key', usernameVariable: 'DOCKER_HUB_USER', passwordVariable: 'DOCKER_HUB_PASS')]) {
                    sh """
                        echo ${DOCKER_HUB_PASS} | docker login -u ${DOCKER_HUB_USER} --password-stdin
                        docker push vkarpenko02/mr:${GIT_COMMIT}
                    """
                }
            }
        }
    }
}
