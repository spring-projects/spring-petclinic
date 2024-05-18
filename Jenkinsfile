
pipeline {
    agent any

    environment {
        NEXUS_CREDS = credentials('nexus-cred')
        NEXUS_DOCKER_REPO_MR = '34.245.131.115:8085'
        NEXUS_DOCKER_REPO_MAIN = '34.245.131.115:8084'
    }

    tools {
        gradle '8.7'
    }

    stages {
        // Merge request pipeline
        stage('Checkstyle') {
            when {
                changeRequest()
            }
            steps{
                echo 'Running gradle checkstyle'
                sh './gradlew checkstyleMain --no-daemon'
            }
            post {
                always {
                    archiveArtifacts artifacts: 'build/reports/checkstyle/*.xml', fingerprint: true
                }
            }
        }
        stage('Test') {
            when {
                changeRequest()
            }
            steps {
                echo 'Running gradle test'
                sh './gradlew test -x test --no-daemon'
            }
        }
        stage('Build') {
            when {
                changeRequest()
            }
            steps {
                echo 'Running build automation'
                sh './gradlew build -x test -x check -x checkFormat -x processTestAot --no-daemon'
                archiveArtifacts artifacts: 'build/libs/*.jar', fingerprint: true            }
        }
        stage('Docker Build (MR)') {
            when {
                changeRequest()
            }
            steps { 
                echo 'Building docker Image'
                sh 'docker build -t $NEXUS_DOCKER_REPO_MR/spring-petclinic:${GIT_COMMIT} .'
            }
        }
        stage('Docker Login (MR)') {
            when {
                changeRequest()
            }
            steps {
                echo 'Nexus Docker Repository Login'
                script{
                    withCredentials([usernamePassword(credentialsId: 'nexus-cred', usernameVariable: 'USER', passwordVariable: 'PASS' )]){
                        sh 'echo $PASS | docker login -u $USER --password-stdin $NEXUS_DOCKER_REPO_MR'
                    }
                    
                }
            }
        }
        stage('Docker Push (MR)') {
            when {
                changeRequest()
            }
            steps {
                echo 'Pushing Image to docker repo'
                sh 'docker push $NEXUS_DOCKER_REPO_MR/spring-petclinic:${GIT_COMMIT}'
            }
        }

        // Main branch pipeline
        stage('Docker Build (Main)') {
            when {
                branch 'main'
            }
            steps { 
                echo 'Building docker Image'
                sh 'docker build -t $NEXUS_DOCKER_REPO_MAIN/spring-petclinic:${GIT_COMMIT} .'
            }
        }
        stage('Docker Login (Main)') {
            when {
                branch 'main'
            }
            steps {
                echo 'Nexus Docker Repository Login'
                script{
                    withCredentials([usernamePassword(credentialsId: 'nexus-cred', usernameVariable: 'USER', passwordVariable: 'PASS' )]){
                        sh 'echo $PASS | docker login -u $USER --password-stdin $NEXUS_DOCKER_REPO_MAIN'
                    }
                }
            }
        }
        stage('Docker Push (Main)') {
            when {
                branch 'main'
            }
            steps {
                echo 'Pushing Image to docker repo'
                sh 'docker push $NEXUS_DOCKER_REPO_MAIN/spring-petclinic:${GIT_COMMIT}'
            }
        }
    }
    post {
        always {
            cleanWs()
        }
    }
}