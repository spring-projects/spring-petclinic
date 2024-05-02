// Merge request pipeline
pipeline {
    agent any
    environment {
        NEXUS_CREDS = credentials('nexus-cred')
        NEXUS_DOCKER_REPO_MR = '34.241.46.54:8085'
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
            post {
                always {
                    archiveArtifacts artifacts: 'build/reports/checkstyle/*.xml', fingerprint: true
                }
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
        stage('Docker Build (MR)') {
            steps { 
                echo 'Building docker Image'
                sh 'docker build -t $NEXUS_DOCKER_REPO_MR/spring-petclinic:${GIT_COMMIT} .'
            }
        }
        stage('Docker Login') {
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
            steps {
                echo 'Pushing Image to docker hub'
                sh 'docker push $NEXUS_DOCKER_REPO_MR/spring-petclinic:${GIT_COMMIT}'
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}

// Main branch pipeline
pipeline {
    agent any

    environment {
        NEXUS_CREDS = credentials('nexus-cred')
        NEXUS_DOCKER_REPO_MAIN = '34.241.46.54:8084'
    }

    stage('Docker Build (Main)') {
        when {
            branch 'main'
        }
        steps { 
            echo 'Building docker Image'
            sh 'docker build -t $NEXUS_DOCKER_REPO_MAIN/spring-petclinic:${GIT_COMMIT} .'
        }
    }
    stage('Docker Login') {
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
            echo 'Pushing Image to docker hub'
            sh 'docker push $NEXUS_DOCKER_REPO_MAIN/spring-petclinic:${GIT_COMMIT}'
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}