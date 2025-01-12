pipeline {
    agent any
    triggers {
        pollSCM('H/5 * * * *') // Watches the `dev` branch
    }
    environment {
        DOCKERHUB_CREDENTIALS = credentials('nalexx6-dockerhub-pass')
        DOCKERHUB_USERNAME = "nalexxgd"
        NEXUS_URL = 'localhost:8081'
    }
    stages {
        stage('Checkout Code') {
            steps {
                git {
                    url: 'git@github.com:Nalexx06/spring-petclinic.git'
                    credentialsId: "nalexx06_github_ssh"
                    branch: 'dev'
                }
            }
        }
        stage('Build & Test') {
            steps {
                sh './mvnw clean verify'
            }
        }
        stage('Upload to Nexus') {
            steps {
                sh './mvnw deploy -DaltDeploymentRepository=snapshots::default::${NEXUS_URL}'
            }
        }
        stage('Build Docker Image') {
            steps {
                script {
                    def version = sh(script: "mvn help:evaluate -Dexpression=project.version -q -DforceStdout", returnStdout: true).trim()
                    sh "docker build -t petclinic:${version} ."
                    sh "docker tag petclinic:${version} ${DOCKERHUB_USERNAME}/petclinic:${version}"
                    sh "docker login -u ${DOCKERHUB_USERNAME} -p ${DOCKERHUB_CREDENTIALS}"
                    sh "docker push ${DOCKERHUB_USERNAME}/petclinic:${version}"
                }
            }
        }
    }
}
