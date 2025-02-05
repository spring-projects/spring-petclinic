pipeline {
  agent {
    label 'worker5'
  }

  environment {
    imageName = "spring-pet-clinic"

    registryCredentials = "nexus-credentials"
    registry = "localhost:9081"
    dockerImage = ''
    DOCKERHUB_CREDENTIALS="piachsecki-dockerhub"
  }

  tools {
    maven 'm3'
  }

  stages {


    stage ('Docker build') {
      steps {
       script {
          sh '/usr/sbin/envShell.sh build -t piachsecki/spring-pet-clinic:latest .'

        }
      }

    }
    stage('Login to dockerhub') {
      steps {
        sh 'echo $DOCKERHUB_CREDENTIALS_PSW | docker  login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin'
   }
   }
    stage('Push') {
      steps {
        sh 'docker push piachsecki/spring-pet-clinic:latest'
      }

    }


    /*stage ('Build') {
      steps {
        sh './mvnw -B -DskipTests clean package'
      }
    }

    stage ('Test') { 
      steps {
        sh './mvnw test'
      }
    }

    stage ('Deploy') {
      steps {
        sh 'echo "hello $USER"'
      }
    }
*/
  }
}
