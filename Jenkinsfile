pipeline {
    agent {
        label 'k8s'
    }

    options {
        buildDiscarder(logRotator(numToKeepStr: '15', daysToKeepStr: '30'))
    }

    environment {
        DOCKER_REGISTRY_HOST = 'gcr.io'
    }

    stages {
        stage ('Pre-Build :: Set Version') {
            steps {
                script {
                    env.POM_VERSION = readMavenPom(file: "${WORKSPACE}/pom.xml").version
                    env.RELEASE_VERSION = env.POM_VERSION.replace("-SNAPSHOT", "")
                    env.BUILD_VERSION = "${env.RELEASE_VERSION}-${env.BUILD_TIMESTAMP}-${env.BUILD_NUMBER}"

                    currentBuild.displayName = "${env.BRANCH_NAME} - ${env.BUILD_VERSION}"
                }

                sh 'cd ${WORKSPACE} && mvn versions:set -DnewVersion=${BUILD_VERSION}'
            }
        }

        stage ('Build :: Build Artifacts') {
            steps {
                sh 'cd ${WORKSPACE} && mvn -C -B clean install -Ddocker.registry.host=${DOCKER_REGISTRY_HOST} -Pimage'
            }
        }

        stage ('Publish code coverage results') {
            steps {
                jacoco()
            }
        }

        stage ('Promote :: Push Docker Images to GCR') {
            when {
                branch 'master'
            }

            steps {
                sh 'gcloud docker -a'
                sh 'mvn -C -B deploy -DskipTests -Ddocker.skip.build=true -Ddocker.registry.host=${DOCKER_REGISTRY_HOST} -Pimage'
            }
        }

        stage('Deploy :: Deploy to STAGE env') {
            when {
                branch 'master'
                environment name: 'STAGE_FREEZE', value: 'false'
            }

            steps {
                build job: 'TODO'
            }
        }
    }

    post {
        always {
            sh 'docker rmi $(docker images | grep ${BUILD_VERSION} | awk \'{ print $3 }\')'

            deleteDir()
        }
    }
}
