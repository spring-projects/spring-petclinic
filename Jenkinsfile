pipeliene {
    agent any
    triggers { pollSCM '* * * * *' }
    stages {
        stage( 'version control sysytem') {
            steps {
            git url: 'https://github.com/spring-projects/spring-petclinic.git',
                branch: 'main'
            }
        }
        stage ('package') {
            steps {
                sh 'mvn package'
            }
        }
    }
}    
