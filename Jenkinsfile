pipeline {
  agent any

  tools {
    maven 'maven-3' // your Maven tool label
  }

  environment {
    ARTIFACTORY_URL = 'http://localhost:8081/artifactory'
    BUILD_NAME = "spring-petclinic"
    BUILD_NUMBER = "${BUILD_ID}"
    DOCKER_IMAGE = "localhost:8081/petclinic-docker-dev-local/spring-petclinic:${BUILD_ID}"
  }

  stages {
    stage('Clone Repo') {
      steps {
        git branch: 'main', url: 'https://github.com/your-org/your-repo.git'
      }
    }

    stage('Build with Maven') {
      steps {
        rtMavenRun (
          tool: 'maven-3',
          pom: 'pom.xml',
          goals: 'clean install -DskipTests=true -Denforcer.skip=true',
          resolverId: 'maven-resolver',
          deployerId: 'maven-deployer'
        )
      }
    }

    stage('Docker Build and Push') {
      steps {
        script {
          docker.build("${DOCKER_IMAGE}")
          def server = Artifactory.server('artifactory-creds')
          server.dockerPush(
            image: "${DOCKER_IMAGE}",
            targetRepo: 'petclinic-docker-dev-local',
            buildInfo: Artifactory.newBuildInfo().name("${BUILD_NAME}").number("${BUILD_NUMBER}")
          )
        }
      }
    }

    stage('Publish Build Info') {
      steps {
        script {
          def server = Artifactory.server('artifactory-creds')
          def buildInfo = Artifactory.newBuildInfo()
          buildInfo.name = "${BUILD_NAME}"
          buildInfo.number = "${BUILD_NUMBER}"
          server.publishBuildInfo(buildInfo)
        }
      }
    }
  }
}
