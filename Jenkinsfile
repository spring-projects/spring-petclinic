pipeline {
    agent any

    environment {
        DOCKER_CREDENTIALS = credentials('dockercreds')
    }

    stages {
        stage('Docker Login') {
            steps {
                script {
                    withEnv(["DOCKER_USERNAME=${DOCKER_CREDENTIALS_USR}", "DOCKER_PASSWORD=${DOCKER_CREDENTIALS_PSW}"]) {
                        sh "docker login -u $DOCKER_USERNAME -p $DOCKER_PASSWORD"
                    }
                }
            }
        }
        stage('Checkstyle') {
            when {
                expression { env.CHANGE_ID != null } // Run only for merge requests
            }
            steps {
                sh './mvnw checkstyle:checkstyle'
            }
            post {
                always {
                    archiveArtifacts artifacts: '**/target/checkstyle-result.xml', allowEmptyArchive: true
                }
            }
        }
        stage('Test') {
            when {
                expression { env.CHANGE_ID != null } // Run only for merge requests
            }
            steps {
                sh './mvnw test'
            }
        }
        stage('Build Without Tests') {
            when {
                expression { env.CHANGE_ID != null } // Run only for merge requests
            }
            steps {
                sh './mvnw clean package -DskipTests'
            }
        }
        stage('Create Docker Image for MR') {
            when {
                expression { env.CHANGE_ID != null } // Run only for merge requests
            }
            steps {
                script {
                    def commitId = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
                    sh "docker build -t mivancevic/mr:${commitId} ."
                    sh "docker push mivancevic/mr:${commitId}"
                }
            }
        }
        stage('Create Docker Image for Main') {
            when {
                branch 'main' // Run only for the main branch
            }
            steps {
                script {
                    def commitId = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
                    sh "docker build -t mivancevic/main:${commitId} ."
                    sh "docker push mivancevic/main:${commitId}"
                }
            }
        }
    }
}
