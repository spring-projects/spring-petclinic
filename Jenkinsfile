pipeline {
  agent any
  tools {
    jdk 'jdk17'
    maven 'M3'
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
       sh 'mvn -Dmaven.test.failure.igmore=ture clean package '
        }
    post {
        success {
          junit 'target/surefire-reports/**/*.xml'
        }
    }
   }
       stage('SSH Publish') {
          steps {
            echo 'SSH publish'
            sshPublisher(publishers: [sshPublisherDesc(configName: 'target', 
                transfers: [sshTransfer(cleanRemote: false, 
                excludes: '', 
                execCommand: '''
                fuser -k 8080/tcp
                export BUILD_ID=Pipeline-Test
                nohup java -jar /home/ubuntu/deploy/spring-petclinic-2.4.0.BUILD-SNAPSHOT.jar >> nohup.out 2>&1 &''', 
                execTimeout: 120000, 
                flatten: false, 
                makeEmptyDirs: false, 
                noDefaultExcludes: false, 
                patternSeparator: '[, ]+', 
                remoteDirectory: 'deploy', 
                remoteDirectorySDF: false, 
                removePrefix: 'target', 
                sourceFiles: 'target/*.jar')], 
                usePromotionTimestamp: false, 
                useWorkspaceInPromotion: false, verbose: false)])

            }
        }
  
  
  
  }
}
