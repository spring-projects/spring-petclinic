pipeline {
  agent any

  tools {
    maven "M3"
    jdk "JDK17"
  }

  environment {
    DOCKERHUB_CREDENTIALS = credentials('dockerCredentials')
  }
  
  stages{
    stage('Git Clone'){
      steps {
        git url: 'https://github.com/sjh4616/spring-petclinic.git', branch: 'main'
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
      steps{
        sh 'docker push wodnr8174/spring-petclinic:latest'
      }  
    }
    stage('Docker Image Remove') {
      steps {
        sh 'docker rmi wodnr8174/spring-petclinic:$BUILD_NUMBER wodnr8174/spring-petclinic:latest
      }  
    }
    stage('Publish Over SSH') {
      steps {
      sshPublisher(publishers: [sshPublisherDesc(configName: 'target', transfers: [sshTransfer(cleanRemote: false, excludes: '', execCommand: '''docker rm -f $(docker ps -aq)
      docker rm $(docker images -q)

      docker run -itd -p 8080:8080 --name=spring-petclinic wodnr8174/spring-petclinic:latest
      ''', execTimeout: 120000, flatten: false, makeEmptyDirs: false, noDefaultExcludes: false, patternSeparator: '[, ]+', remoteDirectory: '', remoteDirectorySDF: false, removePrefix: '', sourceFiles: 'target'), sshTransfer(cleanRemote: false, excludes: '', execCommand: '', execTimeout: 120000, flatten: false, makeEmptyDirs: false, noDefaultExcludes: false, patternSeparator: '[, ]+', remoteDirectory: '', remoteDirectorySDF: false, removePrefix: '', sourceFiles: '')], usePromotionTimestamp: false, useWorkspaceInPromotion: false, verbose: false)])
      }
    }
  }
}




