pipeline {
    agent any
    tools {
        maven 'maven-3'
    }
    environment {
        JFROG_URL = "https://trialt0zppb.jfrog.io"
        JFROG_REPO_RELEASES = "petclinic-maven-dev-local"
        JFROG_REPO_SNAPSHOTS = "petclinic-maven-dev-virtual"
        JFROG_CREDENTIALS_ID = 'jfrog-saas'  // Jenkins credential ID (username + API key or password)
        JFROG_CLI_BUILD_NAME = "spring-petclinic"
        JFROG_CLI_BUILD_NUMBER = "${BUILD_ID}"
    }
    stages {
        stage('Configure JFrog CLI') {
            steps {
                withCredentials([usernamePassword(credentialsId: "${env.JFROG_CREDENTIALS_ID}", usernameVariable: 'JFROG_USER', passwordVariable: 'JFROG_API_KEY')]) {
                    sh """
                        jfrog config add jenkins-config \\
                            --url=${JFROG_URL} \\
                            --user=$JFROG_USER \\
                            --apikey=$JFROG_API_KEY \\
                            --interactive=false
                    """
                }
            }
        }

        stage('Build Maven') {
            steps {
                sh 'mvn clean install -DskipTests -Dcheckstyle.skip=true'
            }
        }

        stage('Upload to Artifactory') {
            steps {
                sh """
                    jfrog rt u "target/*.jar" ${JFROG_REPO_RELEASES}/ \\
                        --build-name=${JFROG_CLI_BUILD_NAME} \\
                        --build-number=${JFROG_CLI_BUILD_NUMBER}
                """
            }
        }

        stage('Publish Build Info') {
            steps {
                sh """
                    jfrog rt bp ${JFROG_CLI_BUILD_NAME} ${JFROG_CLI_BUILD_NUMBER}
                """
            }
        }
    }
    post {
        always {
            echo "Build complete: ${env.JFROG_CLI_BUILD_NAME} #${env.BUILD_NUMBER}"
        }
    }
}
