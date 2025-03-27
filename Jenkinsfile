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
                    // Using JFrog plugin to capture build info
                    artifactory.upload spec: """{
                        "files": [
                            {
                                "pattern": "target/*.jar",
                                "target": "${JFROG_REPO_RELEASES}/"
                            }
                        ]
                    }"""
                    
                    // Run Maven Build
                    sh 'mvn clean deploy -DskipTests -Dcheckstyle.skip=true'
                }
            }
        }
        stage('Publish Build Info') {
            steps {
                script {
                    // Publish build information to JFrog Artifactory
                    def buildInfo = Artifactory.newBuildInfo()
                    buildInfo.name = "${JFROG_CLI_BUILD_NAME}"
                    buildInfo.number = "${JFROG_CLI_BUILD_NUMBER}"
                    
                    Artifactory.publishBuildInfo(buildInfo)
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