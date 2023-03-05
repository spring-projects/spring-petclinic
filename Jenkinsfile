pipeline{ 
    agent { label 's3'}
    triggers {
        pollSCM('* * * * *')
    }
      stages {
         stage ('vcs') {
            steps {
                git branch: 'main',
                  url: 'https://github.com/shaiksohail11/spring-petclinic.git',
                    sh 'mvn package'


            }

         }
      }
}