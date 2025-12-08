pipeline {
  //agent any
  agent none
  
  tools {
    maven "M3"
    jdk "JDK17"
  }

  environment {
    DOCKERHUB_CREDENTIALS = credentials('Docker-token') 
  }

  stages{
    /*stage('Git Clone'){
      steps {
        git url: 'https://github.com/wodnr533/spring-petclinic.git', branch: 'main'
      }
    */}
    stage('Maven Build'){
      agent {
        docker {
          image 'maven:3.9.5-openjdk-17-slim'
        }
      }
      steps {
          sh 'mvn -Dmaven.test.failure.ignore=true clean package'
      }
    }
    stage('Docker Image Create') {
      agent {
        docker {
          image 'docker:24.0.5-cli-alpine'
          args '-v /var/run/docker.sock:/var/run/docker.sock'
        }
      }
      steps {
        sh """
        docker build -t wodnr8174/spring-petclinic:$BUILD_NUMBER .
        docker tag wodnr8174/spring-petclinic:$BUILD_NUMBER wodnr8174/spring-petclinic:latest
        """
      }
    }
    stage('Docker Hub Login') {
      agent { docker { image 'docker:24.0.5-cli-alpine'; args '-v /var/run/docker.sock:/var/run/docker.sock' } }
      steps {
        sh 'echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin'
      }
    }
    stage('Docker Image Push') {
      agent { docker { image 'docker:24.0.5-cli-alpine'; args '-v /var/run/docker.sock:/var/run/docker.sock' } }
      steps {
        sh 'docker push wodnr8174/spring-petclinic:latest'
      }
    }
    stage('Docker Image Remove') {
      agent { docker { image 'docker:24.0.5-cli-alpine'; args '-v /var/run/docker.sock:/var/run/docker.sock' } }
      steps {
        sh 'docker rmi wodnr8174/spring-petclinic:$BUILD_NUMBER wodnr8174/spring-petclinic:latest'
      }
    }
    stage('Kubernetes Deploy') {
      agent {
        docker {
          image 'bitnami/kubectl:latest'
        }
      }
      steps {
        withKubeConfig([credentialsId: 'kubeconfig']) {
          sh 'kubectl apply -f postgres.yml'
          sh 'kubectl apply -f petclinic-deployment.yaml'
          sh 'kubectl apply -f petclinic-service.yaml'
          sh "kubectl set image deployment/petclinic workload=wodnr8174/spring-petclinic:latest"
        }
      }
    }
  }
}
