pipeline {
  agent any
  tools {
    jdk 'jdk17'
    maven 'M3'
  }
 
  environment {
    AWS_CREDENTIAL_NAME=AWSCredentials
    REGION = "ap-northest"
    DOCKER_IMAGE_NAME = "std01-spring-petclinic"
    ECR_REPOSITORY = "257307634175.dkr.ecr.ap-northeast-2.amazonaws.com"
    ECR_DOCKER_IMAGE = "${ECR_REPOSITORY}/${DOCKER_IMAGE_NAME}"
  }
  
  stages {
    stage('Git clone') {
      steps {
        echo 'Git clone'
        git url: 'https://github.com/lwj9812/spring-petclinic.git',
        branch: 'efficient-webjars'
      } 
    post {
      success {
          echo 'Git Clone Success!!'
      }
      failure {
          echo 'Git Clone Fail'
      }
    }
  }
   stage('Mvnen Build') {
     steps {
       echo 'Maven Build'
       sh 'mvn -Dmaven.test.failure.igmore=ture clean package'
        }
    post {
        success {
          junit 'target/surefire-reports/**/*.xml'
        }
     }
   }

    stage ('Docker Image Build'){
      steps {
        echo Docker Image Build'
        dir("${env.WORKSPACE}") {
          sh """
              docker build -t $ECR_DOCKER_IMAGE:$BUILD_NUMBER .
              docker tag $ECR_DOCKER_IMAGE:$BUILD_NUMBER $ECR_DOCKER_IMAGE:latest
          """
        }
      }
      
    }

    
  }
}
