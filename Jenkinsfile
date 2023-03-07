pipeliene {
    agent { label 'JDK_17' }
    triggers { pollSCM '* * * * *' }
    stages {
        stage( 'version control sysytem') {
            steps {
            git url: 'https://github.com/spring-projects/spring-petclinic.git',
                branch: 'main'
            }
        }
        stage( 'post build' ) {
            steps {
                archiveArtifacts artifacts: '**/libs/spring-petclinic-3.0.0/jar'
                                 onlyIfSuccesful: true
                junit testResults: '**/test-results/test/TEST-*.xml'                 
            }
        }
    }
}    
