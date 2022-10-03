pipeline {
  agent any
  stages {
    stage('Build using Maven') {
      steps {
        sh 'mvn package'
      }
    }

    stage('build docker image') {
      steps {
        sh '''docker build -t mypetclinic:$BUILD_ID .
docker image tag mypetclinic:$BUILD_ID mypetclinic/mypetclinic:latest
docker push -a mypetclinic/mypetclinic'''
      }
    }

  }
}