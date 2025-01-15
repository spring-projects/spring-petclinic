pipeline {
    agent {
        label 'agent1'
    }
    parameters {
        string(name: 'SNAPSHOT_VERSION', description: 'Snapshot version to promote')
    }
    environment {
        DOCKERHUB_CREDENTIALS = credentials('nalexx6_docker_pass')
        DOCKERHUB_USERNAME = "nalexx6"
        NEXUS_SNAPHOT_URL = 'http://localhost:8081/repository/maven-snapshots'
        NEXUS_RELEASE_URL = 'http://localhost:8081/repository/maven-releases'
        def RELEASE_VERSION = SNAPSHOT_VERSION.replace('-SNAPSHOT', '')
        ARTIFACT_PATH = "."
    }
    stages {
        stage('Checkout Code') {
            steps {
                git url: 'https://github.com/Nalexx06/spring-petclinic.git', branch: 'dev'

            }
        }
        stage('Download Snapshot Artifact') {
            steps {
                sh "curl -o petclinic-${RELEASE_VERSION}.jar ${NEXUS_SNAPHOT_URL}/petclinic-${SNAPSHOT_VERSION}.jar "
            }
        }
        stage('Promote Artifact to Release') {
            steps {
                script {
                    sh "./mvnw deploy:deploy-file -DgroupId=org -DartifactId=petclinic -Dversion=${RELEASE_VERSION} -Dpackaging=jar -Dfile=petclinic-${RELEASE_VERSION}.jar -DrepositoryId=maven-releases -Durl=${NEXUS_RELEASE_URL} --settings settings.xml"
                }
            }
        }
        stage('Build Docker Image for Release') {
            steps {
                script {
                    sh "DOCKER_BUILDKIT=0 docker build --build-arg ARTIFACT_VERSION=${RELEASE_VERSION} --build-arg ARTIFACT_PATH=${ARTIFACT_PATH} -t petclinic:${RELEASE_VERSION} ."
                    sh "docker tag petclinic:${RELEASE_VERSION}  ${DOCKERHUB_USERNAME}/petclinic:${RELEASE_VERSION}"
                    sh "docker login -u  ${DOCKERHUB_USERNAME} -p ${DOCKERHUB_CREDENTIALS}"
                    sh "docker push  ${DOCKERHUB_USERNAME}/petclinic:${RELEASE_VERSION}"
                }
            }
        }
        stage('Trigger Deployment') {
            steps {
                build job: 'deploy', parameters: [string(name: 'ARTIFACT_VERSION', value: RELEASE_VERSION)]
            }
        }
    }
}
