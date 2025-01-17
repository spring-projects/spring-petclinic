pipeline {
    agent any

    parameters {
        string(name: "DOCKERHUB_CREDENTIALS_ID", description: "DockerHub credentials ID")
    }

    environment {
        DOCKERHUB_REPO_MR = "mr-jenkins"
        DOCKERHUB_REPO_MAIN = "main-jenkins"
    }

    stages {
        stage("Checkstyle") {
            when {
                expression { env.BRANCH_NAME != 'main' }
            }
            steps {
                withMaven(maven: 'maven-3.9.8') {
                    sh './mvnw checkstyle:checkstyle -Dcheckstyle.output.file=target/checkstyle-result.xml'
                }
            }
            post {
                always {
                    archiveArtifacts artifacts: 'target/checkstyle-result.xml', allowEmptyArchive: false
                }
            }
        }

        stage("Test") {
            when {
                expression { env.BRANCH_NAME != 'main' }
            }
            steps {
                withMaven(maven: 'maven-3.9.8') {
                    sh './mvnw clean test'
                }
            }
        }

        stage("Build") {
            when {
                expression { env.BRANCH_NAME != 'main' }
            }
            steps {
                withMaven(maven: 'maven-3.9.8') {
                    sh './mvnw clean package -DskipTests'
                }
            }
        }

        stage("Create Docker Image") {
            steps {
                script {
                    def imageName = "${env.DOCKERHUB_REPO_MR}:${env.GIT_COMMIT.substring(0, 7)}"
                    if (env.BRANCH_NAME == 'main') {
                        imageName = "${env.DOCKERHUB_REPO_MAIN}:${env.GIT_COMMIT.substring(0, 7)}"
                    }
                    docker.build(imageName)
                    docker.withRegistry('https://index.docker.io/v1/', "${params.DOCKERHUB_CREDENTIALS_ID}") {
                        docker.image(imageName).push()
                    }
                }
            }
        }
    }
}
