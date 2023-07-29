pipeline {
   agent any
   options {
     timeout(time: 30, unit: 'MINUTES')
   }
   triggers {
     pollSCM('* * * * *')
   }
   tools {
    jdk 'JDK_17'
   }
   stages {
    stage('vcs') {
      steps {
        git url: 'https://github.com/shaifalikhan5/spring-petclinic.git',
         branch: 'developer'

      }
    }
    stage('build and packaging') {
         steps {
            sh script: 'mvn package'
        
      }

    }
    stage('reporting') {
         steps {
            archiveArtifacts artifacts : '**/target/sprigpetclinic-*.jar'
            junit testResults : '**/surefire-reports/**.xml'
        
      }

    }
   }

}