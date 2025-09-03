pipeline {
  agent any

  tools {
    maven "M3"
    jdk "JDK17"
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
    stage('Publish over SSH') {
      steps {
        sshPublisher(publishers: [sshPublisherDesc(configName: 'target',
        transfers: [sshTransfer(cleanRemote: false, 
        excludes: '', 
        execCommand: '''
        fuser -k 8080/tcp
        export BUILD_ID=Pipeline-PetClinic
        nohup java -jar /home/ubuntu/deploy/spring-petclinic-3.5.0-SNAPSHOT.jar >> nohup.out 2>&1 &''',
        execTimeout: 120000, 
        flatten: false, 
        makeEmptyDirs: false, 
        noDefaultExcludes: false, 
        patternSeparator: '[, ]+', 
        remoteDirectory: '', 
        remoteDirectorySDF: false, 
        removePrefix: 'target', 
        sourceFiles: 'target/*.jar')], 
        usePromotionTimestamp: false, 
        useWorkspaceInPromotion: false, 
        verbose: false)])
      }
    }
  }
}




