pipeline {
    agent { label 'JDK_17' }
    triggers { pollSCM ('* * * * *') }
    }
    stages {
        stage('vcs') {
            steps {
                git url: 'https://github.com/Aseerwadham/spring-petclinic.git',
                    branch: 'main'
            }
        }
        stage('package') {
            tools {
                jdk 'JDK_17'
            }
            steps {
                sh "mvn package"
            }
        }
        stage('post build') {
            steps {
                archiveArtifacts artifacts: '**/target/spring-petclinic-3.0.0-SNAPSHOT.jar',
                                 onlyIfSuccessful: true
                junit testResults: '**/surefire-reports/TEST-*.xml'
            }
        }
    }
}


