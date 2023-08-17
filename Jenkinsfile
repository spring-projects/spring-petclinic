pipeline {
    agent { label 'JDK-17-MVN-3.6' }
    triggers { 
        cron('* * * * *') 
        }
    stages { 
        stage('vcs') {
            steps {
                git url: 'https://github.com/satya36-cpu/spring-petclinicnew.git',
                branch: 'main'
            }
        }
         stage('build and package') {
            steps {
                sh 'clean package'
            }
         }
          stage('archive results') {
            steps {
                junit '**/surefire-reports/*.xml'
            }
        }
    }
}