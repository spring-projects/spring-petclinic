pipeline {
  agent any

  environment {
    JFROG_CLI_BUILD_NAME = "spring-petclinic"
    JFROG_CLI_BUILD_NUMBER = "${BUILD_ID}"
  }

  stages {
    stage('Setup JFrog CLI') {
      steps {
        jfrog rt config --server-id-resolve jfrog-local --server-id-deploy jfrog-local
      }
    }

    stage('Checkout') {
      steps {
        git branch: 'main', url: 'https://github.com/JesseHouldsworth/spring-petclinic.git'
      }
    }

    stage('Build with Maven') {
      steps {
        sh '''
          jf mvnc --global \
            --repo-resolve-releases=libs-release-local \
            --repo-resolve-snapshots=libs-snapshot-local
          jf mvn clean install -DskipTests=true -Denforcer.skip=true
        '''
      }
    }

    stage('Build Docker Image') {
      steps {
        sh """
          docker build -t localhost:8081/petclinic-docker-dev-local/spring-petclinic:${BUILD_ID} .
        """
      }
    }

    stage('Push Docker Image') {
      steps {
        sh """
          jf docker-push \
            localhost:8081/petclinic-docker-dev-local/spring-petclinic:${BUILD_ID} \
            petclinic-docker-dev-local
        """
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
}
