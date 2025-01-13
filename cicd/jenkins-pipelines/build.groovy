pipeline {
    agent any
//    triggers {
//        pollSCM('H/5 * * * *') // Watches the `dev` branch
//    }
    environment {
        DOCKERHUB_CREDENTIALS = credentials('nalexxgd_docker_pass')
        DOCKERHUB_USERNAME = "nalexxgd"
        NEXUS_URL = 'localhost:8081'
        def ARTIFACT_VERSION = sh(script: "mvn help:evaluate -Dexpression=project.version -q -DforceStdout", returnStdout: true).trim()

    }
    stages {
        stage('Checkout Code') {
            steps {
                git url: 'https://github.com/Nalexx06/spring-petclinic.git', branch: 'dev'

            }
        }
        stage('Build & Test') {
            steps {
                sh './mvnw clean verify'
            }
        }
        stage('Upload to Nexus') {
            steps {
                sh """
            mvn deploy:deploy-file -DgroupId=org -DartifactId=petclinic -Dversion=${ARTIFACT_VERSION} -Dpackaging=jar -Dfile=./target/spring-petclinic-${ARTIFACT_VERSION}.jar -DrepositoryId=maven-snapshots -Durl=http://${NEXUS_URL}/repository/maven-snapshots/ --settings settings.xml
                """
            }
        }
        stage('Build Docker Image') {
            steps {
                script {
                    sh "DOCKER_BUILDKIT=0 docker build --build-arg ARTIFACT_VERSION=${ARTIFACT_VERSION} -t petclinic:${ARTIFACT_VERSION} ."
                    sh "docker tag petclinic:${ARTIFACT_VERSION} ${DOCKERHUB_USERNAME}/petclinic:${ARTIFACT_VERSION}"
                    sh "docker login -u ${DOCKERHUB_USERNAME} -p ${DOCKERHUB_CREDENTIALS}"
                    sh "docker push ${DOCKERHUB_USERNAME}/petclinic:${ARTIFACT_VERSION}"
                }
            }
        }
    }
}
