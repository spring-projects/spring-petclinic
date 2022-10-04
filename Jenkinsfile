pipeline {
  agent any
  stages {
    stage('Build using Maven') {
      steps {
        sh 'mvn package'
      }
    }

    stage('build docker image and push') {
      steps {
        sh 'docker build -t mypetclinic:$BUILD_ID . '
        withCredentials([usernamePassword(credentialsId: 'dockerhub', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
          sh ''' docker login -u $USER -p $PASS
            docker image tag mypetclinic:$BUILD_ID param8500/mypetclinic:latest
            docker push -a param8500/mypetclinic '''
          }
        
      }
    }

  }
}