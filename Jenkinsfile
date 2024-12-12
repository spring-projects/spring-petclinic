pipeline {
    agent {
        docker {
            image 'docker:latest'
        }
    }

    environment {
        GIT_COMMIT = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
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
                sh 'docker build -t vkarpenko02/spring-petclinic:${GIT_COMMIT} .'
                sh 'docker push vkarpenko02/mr'
            }
        }
    }
}