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
       stage('copy artifact to s3') {
           steps{
                sh "sudo mkdir -p /tmp/artifactory/${JOB_NAME}/${BUILD_ID} && sudo cp ./target/spring-petclinic-*.jar /tmp/artifactory/${JOB_NAME}/${BUILD_ID}"
                sh "aws s3 sync /tmp/artifactory/${JOB_NAME}/${BUILD_ID}/ s3://spc.war"   
           }
            
	   }
    }
    post{
      success{
         
         archiveArtifacts artifacts: '**/target/spring-petclinic-*.jar'
         junit '**/surefire-reports/TEST-*.xml'

         

      }
    }
}