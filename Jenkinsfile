pipeline {
  agent any

  tools {
    jfrog 'jfrog-cli'
    maven 'maven-3'
  }

  environment {
    JFROG_CLI_BUILD_NAME = "spring-petclinic"
    JFROG_CLI_BUILD_NUMBER = "${BUILD_ID}"
    ARTIFACTORY_URL = "http://artifactory.artifactory.svc.cluster.local:8081/artifactory"
  }

  stages {
    stage('Configure JFrog CLI') {
      steps {
        withCredentials([usernamePassword(credentialsId: 'artifactory-creds', usernameVariable: 'ARTIFACTORY_USER', passwordVariable: 'ARTIFACTORY_PASSWORD')]) {
          sh '''
            jf c show petclinic || jf c add petclinic \
              --url=${ARTIFACTORY_URL} \
              --user=${ARTIFACTORY_USER} \
              --password=${ARTIFACTORY_PASSWORD} \
              --interactive=false
            jf use petclinic
          '''
        }
      }
    }

    stage('Validate Connection') {
      steps {
        sh 'jf rt ping'
      }
    }

    stage('Build Maven') {
      steps {
        sh 'chmod +x mvnw'
        sh '''
          jf mvnc --global \
            --repo-resolve-releases=petclinic-maven-dev-virtual \
            --repo-resolve-snapshots=petclinic-maven-dev-virtual \
            --repo-deploy-releases=petclinic-maven-dev-local \
            --repo-deploy-snapshots=petclinic-maven-dev-local
        '''
        sh 'jf mvn clean deploy -DskipTests -Dcheckstyle.skip=true'
      }
    }

    stage('Publish Build Info') {
      steps {
        sh '''
          jf rt build-collect-env
          jf rt build-add-git
          jf rt build-publish
        '''
      }
    }
  }

  post {
    always {
      echo "Build complete: $JFROG_CLI_BUILD_NAME #$JFROG_CLI_BUILD_NUMBER"
    }
  }
}
