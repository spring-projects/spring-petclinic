pipeline {
    agent any
    environment {
        NEXUS_CREDS = credentials('nexus-cred')
        NEXUS_DOCKER_REPO = '54.170.162.132:8085'
    }

    tools {
        gradle '8.7'
    }

    stages {
        stage('Checkstyle') {
            steps{
                echo 'Running gradle checkstyle'
                sh './gradlew checkstyleMain --no-daemon'
            }
        }
        stage('Test') {
            steps {
                echo 'Running gradle test'
                sh './gradlew test -x test --no-daemon'
            }
        }
        stage('Build') {
            steps {
                echo 'Running build automation'
                sh './gradlew build -x test -x check -x checkFormat -x processTestAot --no-daemon'
                archiveArtifacts artifacts: 'build/libs/*.jar', fingerprint: true            }
        }
        stage('Docker Build') {
        
            steps { 
                echo 'Building docker Image'
                sh 'docker build -t $NEXUS_DOCKER_REPO/spring-petclinic:${GIT_COMMIT} .'
            }
        }

        stage('Docker Login') {
            steps {
                echo 'Nexus Docker Repository Login'
                script{
                    withCredentials([usernamePassword(credentialsId: 'nexus-cred', usernameVariable: 'USER', passwordVariable: 'PASS' )]){
                        sh ' echo $PASS | docker login -u $USER --password-stdin $NEXUS_DOCKER_REPO'
                    }
                    
                }
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}