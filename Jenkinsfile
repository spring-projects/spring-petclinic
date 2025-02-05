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
        docker.withRegistry('https://index.docker.io/v1/', 'piachsecki-dockerhub') {
        echo 'Logged into DockerHub'
      }

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
