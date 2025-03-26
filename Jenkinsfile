pipeline {
  agent any

  tools {
    maven 'maven-3'
  }

  environment {
    JFROG_CLI_BUILD_NAME = "spring-petclinic"
    JFROG_CLI_BUILD_NUMBER = "${BUILD_ID}"
    ARTIFACTORY_URL = "http://artifactory.artifactory.svc.cluster.local:8081"
    JF = "${WORKSPACE}/jf"
  }

  stages {

    stage('Download JFrog CLI') {
      steps {
        sh '''
          curl -fL https://releases.jfrog.io/artifactory/jfrog-cli/v2-jf/2.74.1/jfrog-cli-linux-arm64/jf -o "$JF"
          chmod +x "$JF"
        '''
      }
    }

    stage('Configure JFrog CLI') {
      steps {
        withCredentials([usernamePassword(credentialsId: 'jfrog-platform-creds', usernameVariable: 'ARTIFACTORY_USER', passwordVariable: 'ARTIFACTORY_PASSWORD')]) {
          sh '''
            "$JF" c add petclinic \
              --url="${ARTIFACTORY_URL}" \
              --user="$ARTIFACTORY_USER" \
              --password="$ARTIFACTORY_PASSWORD" \
              --interactive=false
              --enc-password=false
          '''
        }
      }
    }

    stage('Validate Connection') {
      steps {
        sh '"$JF" c use petclinic'
        sh '"$JF" rt ping'
      }
    }

    stage('Build Maven') {
      steps {
        sh 'chmod +x mvnw'
        sh '''
          "$JF" mvnc --global \
            --repo-resolve-releases=petclinic-maven-dev-virtual \
            --repo-resolve-snapshots=petclinic-maven-dev-virtual \
            --repo-deploy-releases=petclinic-maven-dev-local \
            --repo-deploy-snapshots=petclinic-maven-dev-local
        '''
        sh '"$JF" mvn clean deploy -DskipTests -Dcheckstyle.skip=true'
      }
    }

    stage('Publish Build Info') {
      steps {
        sh '"$JF" rt build-collect-env'
        sh '"$JF" rt build-add-git'
        sh '"$JF" rt build-publish'
      }
    }
  }

  post {
    always {
      echo "Build complete: ${env.JFROG_CLI_BUILD_NAME} #${env.BUILD_NUMBER}"
    }
  }
}
