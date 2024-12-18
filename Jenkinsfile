pipeline {
    agent any

    environment {
        GIT_COMMIT = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
        DOCKER_CREDS = credentials('docker_key')
    }

    stages {
        stage ('Checkstyle') {
            when { not { branch 'main' } }
            steps {
                sh 'mvn checkstyle:checkstyle'
                archiveArtifacts artifacts: 'target/checkstyle-report.xml', allowEmptyArchive: true
            }
        }
        stage ('Test') {
            when { not { branch 'main' } }
            steps {
                sh 'mvn test'
            }
        }
        stage ('Build') {
            when { not { branch 'main' } }
            steps {
                sh 'mvn clean package -DskipTests'
                archiveArtifacts artifacts: 'target/*.jar', allowEmptyArchive: false
            }
        }
        stage ('Creating Docker Image') {
            steps {
                script {
                    def dockerRepo = branch == 'main' ? 'main' : 'mr'
                    sh "docker build -t vkarpenko02/${dockerRepo}:${GIT_COMMIT} ."
                    withCredentials([usernamePassword(credentialsId: 'docker_key', usernameVariable: 'DOCKER_HUB_USER', passwordVariable: 'DOCKER_HUB_PASS')]) {
                        sh """
                            echo ${DOCKER_HUB_PASS} | docker login -u ${DOCKER_HUB_USER} --password-stdin
                            docker push vkarpenko02/${dockerRepo}:${GIT_COMMIT}
                        """
                    }
                }
            }
        }
    }
}
