pipeline {
    agent any
    tools {
        maven 'maven-3'
    }
    environment {
        JFROG_URL = "https://trialt0zppb.jfrog.io/artifactory"
        JFROG_REPO_RELEASES = "petclinic-maven-dev-local"
        JFROG_REPO_SNAPSHOTS = "petclinic-maven-dev-virtual"
        JFROG_CREDENTIALS_ID = 'jfrog-saas'
        JFROG_CLI_BUILD_NAME = "spring-petclinic"
        JFROG_CLI_BUILD_NUMBER = "${BUILD_ID}"
    }
    stages {
        stage('Build Maven') {
            steps {
                script {
                    def server = Artifactory.server(env.JFROG_CREDENTIALS_ID)

                    // Run Maven Build
                    sh 'mvn clean install -DskipTests -Dcheckstyle.skip=true'

                    // Upload artifacts to Artifactory
                    server.upload spec: """{
                        "files": [
                            {
                                "pattern": "target/*.jar",
                                "target": "${JFROG_REPO_RELEASES}/"
                            }
                        ]
                    }"""
                }
            }
        }
        stage('Publish Build Info') {
            steps {
                script {
                    def server = Artifactory.server(env.JFROG_CREDENTIALS_ID)

                    def buildInfo = Artifactory.newBuildInfo()
                    buildInfo.name = env.JFROG_CLI_BUILD_NAME
                    buildInfo.number = env.JFROG_CLI_BUILD_NUMBER

                    server.publishBuildInfo(buildInfo)
                }
            }
        }
    }
    post {
        always {
            echo "Build complete: ${env.JFROG_CLI_BUILD_NAME} #${env.BUILD_NUMBER}"
        }
    }
}
