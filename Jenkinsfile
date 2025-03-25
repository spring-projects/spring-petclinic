pipeline {
  agent any

  tools {
    // You can still let Jenkins manage Maven, if you want
    maven 'maven-3'
  }

  environment {
    JFROG_CLI_BUILD_NAME = "spring-petclinic"
    JFROG_CLI_BUILD_NUMBER = "${BUILD_ID}"
    // Adjust URL if needed (HTTP vs. HTTPS)
    ARTIFACTORY_URL = "http://artifactory.artifactory.svc.cluster.local:8081/artifactory"
  }

  stages {

    stage('Download JFrog CLI') {
      steps {
        // Download the CLI to "jf"
        sh '''
          curl -fL https://releases.jfrog.io/artifactory/jfrog-cli/v2-jf/latest/jfrog-cli-linux-arm64/jf -o jf
          chmod +x jf
        '''
      }
    }

    stage('Configure JFrog CLI') {
      steps {
        withCredentials([usernamePassword(credentialsId: 'jfrog-platform-creds', usernameVariable: 'ARTIFACTORY_USER', passwordVariable: 'ARTIFACTORY_PASSWORD')]) {
          // Add the config called "petclinic"
          sh '''
            ./jf c add petclinic \
              --url=${ARTIFACTORY_URL} \
              --user=$ARTIFACTORY_USER \
              --password=$ARTIFACTORY_PASSWORD \
              --interactive=false
          '''
        }
      }
    }

    stage('Validate Connection') {
      steps {
        sh './jf c use petclinic'
        sh './jf rt ping'
      }
    }

    stage('Build Maven') {
      steps {
        sh 'chmod +x mvnw'
        // Configure Maven's Artifactory resolver/deployer
        sh '''
          ./jf mvnc --global \
            --repo-resolve-releases=petclinic-maven-dev-virtual \
            --repo-resolve-snapshots=petclinic-maven-dev-virtual \
            --repo-deploy-releases=petclinic-maven-dev-local \
            --repo-deploy-snapshots=petclinic-maven-dev-local
        '''
        // Run the actual build & deploy
        sh './jf mvn clean deploy -DskipTests -Dcheckstyle.skip=true'
      }
    }

    stage('Publish Build Info') {
      steps {
        sh './jf rt build-collect-env'
        sh './jf rt build-add-git'
        sh './jf rt build-publish'
      }
    }
  }

  post {
    always {
      echo "Build complete: ${env.JFROG_CLI_BUILD_NAME} #${env.BUILD_NUMBER}"
    }
  }
}
