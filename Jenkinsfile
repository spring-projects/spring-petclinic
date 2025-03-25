pipeline {
  agent any

  tools {
    jfrog 'jfrog-cli'
    maven 'maven-3'
  }

  environment {
    JFROG_CLI_BUILD_NAME = "spring-petclinic"
    JFROG_CLI_BUILD_NUMBER = "${BUILD_ID}"
  }

  stages {
    stage('Setup') {
      steps {
        jf 'c show'
        jf 'rt ping'
      }
    }

    stage('Build Maven') {
      steps {
        sh 'chmod +x mvnw'
        jf "mvnc --global --repo-resolve-releases=libs-release-local --repo-resolve-snapshots=libs-snapshot-local"
        jf "mvn clean install -DskipTests=true"
      }
    }

    stage('Docker') {
      steps {
        sh "docker build -t localhost:8081/petclinic-docker-dev-local/spring-petclinic:$BUILD_ID ."
        jf "docker-push localhost:8081/petclinic-docker-dev-local/spring-petclinic:$BUILD_ID petclinic-docker-dev-local"
      }
    }

    stage('Publish Build Info') {
      steps {
        jf "rt build-collect-env"
        jf "rt build-add-git"
        jf "rt build-publish"
      }
    }
  }
}
