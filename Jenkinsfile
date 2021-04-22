pipeline {
  agent any
  stages {
    stage('pull') {
      steps {
        git branch: "main",
        credentialsId: 'azima-git-ssh',
        url: 'git://github.com/VSAzima/spring-petclinic'
      }
    }
    stage('build') {
      steps {
        script {
          docker.image('maven:3.3-jdk-8').inside {
          // /var/jenkins_home/workspace/MeFirstPipelineJob
          sh 'mvn -B clean package -Dcheckstyle.skip'
        }
      }
}
}
  //   }
  //   // stage('push') {
  //   //   steps {
  //   //     echo 'Pushing ...'
  //   //
  //   //   }
  //   // }
  // }
}
}
