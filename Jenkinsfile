pipeline{ 
    agent { label 's3'}
    triggers {
        pollSCM('* * * * *')
    }
    stages {
       stage ('vcs') {
          steps {
              git url: 'https://github.com/shaiksohail11/spring-petclinic.git',
                  branch: 'main'
                  sh 'mvn package'
          }
       }
    }
    post{
      success{
         
         archiveArtifacts artifacts: '**/target/spring-petclinic-*.jar'
         junit testResults: '**/surefire-reports/TEST-*.xml'
         

      }
    }
}