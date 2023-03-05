pipeline{ 
    agent { label 's3'}
    triggers {
        pollSCM('* * * * *')
    }
      stages {
         stage ('vcs') {
            steps {
                git url: 'https://github.com/shaiksohail11/spring-petclinic.git',
                  branch: 'main',
                    sh 'mvn package'


            }

         }
      }
}