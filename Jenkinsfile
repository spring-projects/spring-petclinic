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
      steps {
        sh 'docker push wodnr8174/spring-petclinic:latest'
      }
    }
    stage('Docker Image Remove') {
      steps {
        sh 'docker rmi wodnr8174/spring-petclinic:$BUILD_NUMBER wodnr8174/spring-petclinic:latest'
      }
    }
    stage('Publish Over SSH') {
      steps {
        sshPublisher(publishers: [sshPublisherDesc(configName: 'WEB01', 
        transfers: [sshTransfer(cleanRemote: false, 
        excludes: '', 
        execCommand: '''
        fuser -k 8080/tcp
        export BUILD_ID=Petclinic-Pipeline
        nohup java -jar /home/ubuntu/deploy/spring-petclinic-3.5.0-SNAPSHOT.jar >> nohup.out 2>&1 &''',
        execTimeout: 120000, 
        flatten: false, 
        makeEmptyDirs: false, 
        noDefaultExcludes: false, 
        patternSeparator: '[, ]+', 
        remoteDirectory: '', 
        remoteDirectorySDF: false,
        removePrefix: 'WEB01', 
        sourceFiles: 'target/*.jar')], 
        usePromotionTimestamp: false, 
        useWorkspaceInPromotion: false, 
        verbose: false)])
      }
    }
  }
}
