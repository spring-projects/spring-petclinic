pipeline {
  agent any

  tools {
    maven "M3"
    jdk "JDK17"
  }

  environment {
    DOCKERHUB_CREDENTIALS = credentials('Docker-token') 
  }

  stages{
    stage('Git Clone'){
      steps {
        git url: 'https://github.com/wodnr533/spring-petclinic.git'
      }
    }
    stage('Maven Build'){
      steps {
        sh 'mvn -Dmaven.test.failure.ignore=true clean package'
      }
    }
    stage('Docker Image Create') {
      steps {
        sh """
        docker build -t wodnr8174/spring-petclinic:$BUILD_NUMBER .
        docker tag wodnr8174/spring-petclinic:$BUILD_NUMBER wodnr8174/spring-petclinic:latest
        """
      }
    }
    stage('Docker Hub Login') {
      steps {
        sh 'echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin'
      }
    }
    stage('Docker Image Push') {
      steps {
        sh 'docker push wodnr8174/spring-petclinic:latest'
      }
    }
    stage('Docker Image Remove') {
      steps {
        sh 'docker rmi wodnr8174/spring-petclinic:$BUILD_NUMBER wodnr8174/spring-petclinic:latest'
      }
    }
    stage('Kubernetes Deploy') {
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
