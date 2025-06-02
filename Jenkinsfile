pipeline {
    agent { label 'pipeline-agent' } 
    stages {
        stage ('Checkstyle') {
            when {
                changeRequest()
            }
            steps {
                sh './mvnw checkstyle:checkstyle'
            }
            post {
                always {
                    archiveArtifacts artifacts: 'target/checkstyle-result.xml', fingerprint: true
                }
            }
        }
        stage ('Test') {
            when {
                changeRequest()
            }
            steps {
                sh './mvnw test -B'
            }
        }
        stage ('Build') {
            when {
                changeRequest()
            }
            steps {
                sh './mvnw clean package -DskipTests'
            }
        }
        stage ('Docker Push to MR') {
            when {
                changeRequest()
            }
            environment {
                IMAGE = credentials('docker_image_name')
                REGISTRY = credentials('nexus_url_main')
            }
            steps {
                script {
                    def GIT_COMMIT_SHORT = env.GIT_COMMIT.take(7)
                    echo "Git commit short is ${GIT_COMMIT_SHORT}"
                }
            }
        }
        stage ('Docker Push to Main') {
            when {
                branch 'main'
            }
            environment {
                IMAGE = credentials('docker_image_name')
                REGISTRY = credentials('nexus_url_main')
            }
            steps {
                script {
                    def GIT_COMMIT_SHORT = env.GIT_COMMIT.take(7)
                    echo "Git commit short is ${GIT_COMMIT_SHORT}"
                }
            }
        }
    }
}